package com.silverbullet.feature_auth

import com.silverbullet.core.databse.dao.RefreshTokenDao
import com.silverbullet.core.databse.dao.UserDao
import com.silverbullet.core.databse.entity.RefreshTokenEntity
import com.silverbullet.core.databse.utils.DbError
import com.silverbullet.core.databse.utils.DbOperation
import com.silverbullet.core.mapper.toUserInfo
import com.silverbullet.core.security.hashing.HashingEngine
import com.silverbullet.core.security.hashing.SaltedHash
import com.silverbullet.core.security.token.model.TokenClaim
import com.silverbullet.core.security.token.TokenService
import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_auth.exception.InvalidCredentials
import com.silverbullet.feature_auth.exception.InvalidRefreshToken
import com.silverbullet.feature_auth.exception.UserNotFound
import com.silverbullet.feature_auth.exception.UsernameAlreadyExists
import com.silverbullet.feature_auth.request.LoginRequest
import com.silverbullet.feature_auth.request.RefreshTokenRequest
import com.silverbullet.feature_auth.request.SignupRequest
import com.silverbullet.feature_auth.response.LoginResponse
import com.silverbullet.feature_auth.response.SignupResponse
import com.silverbullet.feature_auth.response.TokenInfo
import java.util.Date

class AuthController(
    private val userDao: UserDao,
    private val hashingEngine: HashingEngine,
    private val tokenService: TokenService,
    private val refreshTokenDao: RefreshTokenDao
) {

    /**
     * Signs user up and if there are any issues with the process, it throws an exception which
     * will be handles by the status pages.
     */
    suspend fun processSignupRequest(request: SignupRequest): SignupResponse {
        val saltedHash = hashingEngine.hash(request.password)
        val createUserOp = userDao.insertUser(
            name = request.name,
            username = request.username,
            profilePicUrl = null,
            password = saltedHash.saltedHash,
            salt = saltedHash.salt
        )
        if (createUserOp is DbOperation.Failure) {
            when (createUserOp.error) {
                is DbError.DuplicateKey -> throw UsernameAlreadyExists()
                else -> throw UnexpectedServiceError()
            }
        }
        val userInfo = (createUserOp as DbOperation.Success).data.toUserInfo()
        return SignupResponse(user = userInfo)
    }

    suspend fun processLoginRequest(request: LoginRequest): LoginResponse {
        val user = userDao
            .getUserByUsername(request.username)
            .data ?: throw UserNotFound()
        val saltedHash = SaltedHash(salt = user.salt, saltedHash = user.password)
        val isPasswordValid = hashingEngine.validate(password = request.password, saltedHash = saltedHash)
        if (!isPasswordValid)
            throw InvalidCredentials()
        val tokens = generateUserTokens(user.id)
        // save the user refresh token in the db
        val refreshToken = RefreshTokenEntity(userId = user.id, token = tokens.refreshToken)
        refreshTokenDao.insertRefreshToken(refreshToken)
        return LoginResponse(
            user = user.toUserInfo(),
            tokens = tokens
        )
    }

    suspend fun processRefreshTokenRequest(request: RefreshTokenRequest): TokenInfo {
        val refreshTokenData = tokenService
            .decodeRefreshToken(request.token) ?: throw InvalidRefreshToken()
        val userId = refreshTokenData.userId
        val userToken = refreshTokenDao
            .getTokenByUserId(userId) ?: throw InvalidRefreshToken()
        val currentDate = Date()
        if (userToken.token == request.token && currentDate.before(refreshTokenData.expirationDate)) {
            val newUserTokens = generateUserTokens(userId)
            if(!refreshTokenDao.updateUserRefreshToken(userId,newUserTokens.refreshToken)){
                // Failed to update the refresh token which is unexpected (shouldn't happen)
                throw UnexpectedServiceError()
            }
            return newUserTokens
        }
        // then the refresh token is not valid so throw invalid refresh token exception
        throw InvalidRefreshToken()
    }

    private fun generateUserTokens(userId: Int): TokenInfo {
        val userIdClaim = TokenClaim(key = "userId", value = userId.toString())
        val (accessToken, refreshToken) = tokenService.generateUserTokens(userIdClaim)
        return TokenInfo(accessToken, refreshToken)
    }
}