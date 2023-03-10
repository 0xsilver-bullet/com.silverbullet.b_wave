package com.silverbullet.core.databse.entity

data class UserEntity(
    val id: Int,
    val name: String,
    val username: String,
    val profilePicUrl: String?,
    val password: String,
    val salt: String
)
