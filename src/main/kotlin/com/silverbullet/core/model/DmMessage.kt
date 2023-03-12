package com.silverbullet.core.model

import kotlinx.serialization.Serializable

@Serializable
data class DmMessage(
    val text: String,
    val senderId: Int,
    val receiverId: Int,
    val seen: Boolean,
    val timestamp: Long
)
