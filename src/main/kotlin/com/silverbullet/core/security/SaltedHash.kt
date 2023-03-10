package com.silverbullet.core.security

data class SaltedHash(
    val salt: String,
    val saltedHash: String
)
