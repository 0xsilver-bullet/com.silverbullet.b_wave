package com.silverbullet.core.databse.entity

data class RefreshTokenEntity(
    val userId: Int,
    val token: String
)