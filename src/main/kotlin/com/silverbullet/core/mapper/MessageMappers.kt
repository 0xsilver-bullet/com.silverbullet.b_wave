package com.silverbullet.core.mapper

import com.silverbullet.core.databse.entity.DmMessageEntity
import com.silverbullet.core.model.DmMessage

fun DmMessageEntity.toDmMessage(): DmMessage =
    DmMessage(
        text = text,
        senderId = senderId,
        receiverId = receiverId,
        seen = seen,
        timestamp = timestamp
    )