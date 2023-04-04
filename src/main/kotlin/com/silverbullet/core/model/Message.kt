package com.silverbullet.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val text: String?,
    val imageUrl: String?,
    val senderId: Int,
    val channelId: Int,
    val seenBy: List<Int>,
    val timestamp: Long,
    val id: String
)
