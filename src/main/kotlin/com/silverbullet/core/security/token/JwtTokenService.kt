package com.silverbullet.core.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService(
    private val accessTokenConfig: JwtTokenConfig,
    private val refreshTokenConfig: JwtTokenConfig
) : TokenService {

    override fun generateAccessToken(vararg claims: TokenClaim): String {
        return generateToken(
            expirationMillis = accessTokenConfig.expirationDate,
            secret = accessTokenConfig.secret,
            *claims
        )
    }

    override fun generateRefreshToken(vararg claims: TokenClaim): String {
        return generateToken(
            expirationMillis = refreshTokenConfig.expirationDate,
            secret = refreshTokenConfig.secret,
            *claims
        )
    }

    override fun extractUserId(token: String): Int? {
        return try {
            JWT
                .decode(token)
                .claims["userId"]
                ?.toString()
                ?.replace("\"", "") // it returns a string like ""1"" which fails to convert to int
                ?.toIntOrNull()
        } catch (e: Exception) {
            null
        }
    }

    override fun extractExpirationDate(token: String): Date? {
        return try {
            JWT
                .decode(token)
                .expiresAt
        } catch (e: Exception) {
            null
        }
    }

    private fun generateToken(
        expirationMillis: Long,
        secret: String,
        vararg claims: TokenClaim
    ): String {
        var token =
            JWT
                .create()
                .withIssuer(accessTokenConfig.issuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expirationMillis))
        claims.forEach { claim ->
            token = token.withClaim(claim.key, claim.value)
        }
        return token.sign(Algorithm.HMAC256(secret))
    }
}