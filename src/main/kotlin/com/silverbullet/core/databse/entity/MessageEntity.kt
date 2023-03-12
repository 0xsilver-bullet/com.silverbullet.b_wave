package com.silverbullet.core.databse.entity

data class MessageEntity(
    val text: String,
    val senderId: Int,
    val receiverId: Int,
    val seen: Boolean,
    val timestamp: Long
) {

    companion object {

        fun create(
            text: String,
            senderId: Int,
            receiverId: Int
        ): MessageEntity {
            return MessageEntity(
                text = text,
                senderId = senderId,
                receiverId = receiverId,
                seen = false,
                timestamp = System.currentTimeMillis()
            )
        }
    }
}