package com.silverbullet.core.databse.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

/**
 * @param seenBy a list of user ids who had seen the message.
 */
data class MessageEntity(
    val text: String?,
    val imageUrl: String?,
    val senderId: Int,
    val channelId: Int,
    val seenBy: List<Int> = emptyList(),
    val timestamp: Long = System.currentTimeMillis(),
    @BsonId
    val id: String = ObjectId().toString()
)