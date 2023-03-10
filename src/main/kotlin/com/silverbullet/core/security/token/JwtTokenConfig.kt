package com.silverbullet.core.security.token


data class JwtTokenConfig(
    val audience: String,
    val realm: String,
    val issuer: String,
    val expirationDate: Long,
    val secret: String
)
