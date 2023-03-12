package com.silverbullet.core.security.token

import java.util.Date

interface TokenService {

    fun generateAccessToken(vararg claims: TokenClaim): String

    fun generateRefreshToken(vararg claims: TokenClaim): String

    fun extractUserId(token: String): Int?

    fun extractExpirationDate(token: String): Date?

}