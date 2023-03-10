package com.silverbullet.core.security.hashing

data class SaltedHash(
    val salt: String,
    val saltedHash: String
)
