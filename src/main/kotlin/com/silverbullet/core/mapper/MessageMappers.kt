package com.silverbullet.core.mapper

import com.silverbullet.core.databse.entity.MessageEntity
import com.silverbullet.core.model.Message


fun MessageEntity.toMessage(): Message =
    Message(
        text = text,
        imageUrl = imageUrl,
        senderId = senderId,
        channelId = channelId,
        seenBy = seenBy,
        timestamp = timestamp,
        id = id
    )