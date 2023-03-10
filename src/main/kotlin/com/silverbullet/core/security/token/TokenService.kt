package com.silverbullet.core.security.token

interface TokenService {

    fun generateAccessToken(vararg claims: TokenClaim): String

    fun generateRefreshToken(vararg claims: TokenClaim): String

}