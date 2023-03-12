package com.silverbullet.core.mapper

import com.silverbullet.core.databse.entity.MessageEntity
import com.silverbullet.core.model.DmMessage

fun MessageEntity.toDmMessage(): DmMessage =
    DmMessage(
        text = text,
        senderId = senderId,
        receiverId = receiverId,
        seen = seen,
        timestamp = timestamp
    )