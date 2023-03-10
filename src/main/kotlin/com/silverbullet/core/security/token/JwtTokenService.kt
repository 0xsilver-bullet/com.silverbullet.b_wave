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