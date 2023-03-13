package com.silverbullet.core.databse.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class DmMessageEntity(
    val text: String,
    val senderId: Int,
    val receiverId: Int,
    val seen: Boolean,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
) {

    companion object {

        fun create(
            text: String,
            senderId: Int,
            receiverId: Int
        ): DmMessageEntity {
            return DmMessageEntity(
                text = text,
                senderId = senderId,
                receiverId = receiverId,
                seen = false,
                timestamp = System.currentTimeMillis()
            )
        }
    }
}