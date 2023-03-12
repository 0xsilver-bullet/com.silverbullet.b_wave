package com.silverbullet.core.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.silverbullet.core.security.token.model.JwtTokenConfig
import com.silverbullet.core.security.token.model.RefreshTokenData
import com.silverbullet.core.security.token.model.TokenClaim
import java.util.*

class JwtTokenService(
    private val accessTokenConfig: JwtTokenConfig,
    private val refreshTokenConfig: JwtTokenConfig
) : TokenService {

    override fun generateUserTokens(vararg claims: TokenClaim): Pair<String, String> {
        var baseToken =
            JWT
                .create()
                .withIssuer(accessTokenConfig.issuer)
        claims.forEach { claim ->
            baseToken = baseToken.withClaim(claim.key, claim.value)
        }
        val accessToken =
            baseToken
                .withExpiresAt(Date(System.currentTimeMillis() + accessTokenConfig.expirationDate))
                .sign(Algorithm.HMAC256(accessTokenConfig.secret))
        val refreshToken =
            baseToken
                .withExpiresAt(Date(System.currentTimeMillis() + refreshTokenConfig.expirationDate))
                .sign(Algorithm.HMAC256(refreshTokenConfig.secret))
        return Pair(accessToken, refreshToken)
    }

    override fun decodeRefreshToken(token: String): RefreshTokenData? {
        return try {
            val decodedToken = JWT.decode(token)
            val userId = decodedToken
                .claims["userId"]
                ?.toString()
                ?.replace("\"", "") // it returns a string like ""1"" which fails to convert to int
                ?.toIntOrNull() ?: return null
            RefreshTokenData(
                userId = userId,
                expirationDate = decodedToken.expiresAt
            )
        } catch (e: Exception) {
            null
        }
    }

}