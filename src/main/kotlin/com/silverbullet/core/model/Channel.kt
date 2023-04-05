package com.silverbullet.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    val id: Int,
    val name: String?,
    val type: Int,
    val users: List<UserInfo>
)
