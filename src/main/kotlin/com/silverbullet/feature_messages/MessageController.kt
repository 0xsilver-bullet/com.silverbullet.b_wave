package com.silverbullet.feature_messages

import com.silverbullet.core.databse.dao.MessageDao
import com.silverbullet.core.databse.entity.MessageEntity
import com.silverbullet.core.mapper.toMessage
import com.silverbullet.core.model.Message

class MessageController(private val messageDao: MessageDao) {

    suspend fun getChannelMessages(channelId: Int): List<Message> {
        return messageDao
            .getChannelMessages(channelId)
            .map(MessageEntity::toMessage)
    }
}