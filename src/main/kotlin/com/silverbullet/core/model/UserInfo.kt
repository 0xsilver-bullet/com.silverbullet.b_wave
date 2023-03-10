package com.silverbullet.core.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val name: String,
    val username: String,
    val profilePicUrl: String?,
    val id: Int
)
