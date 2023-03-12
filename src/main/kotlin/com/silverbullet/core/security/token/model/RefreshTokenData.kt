package com.silverbullet.core.security.token.model

import java.util.Date

data class RefreshTokenData(
    val userId: Int,
    val expirationDate: Date
)
