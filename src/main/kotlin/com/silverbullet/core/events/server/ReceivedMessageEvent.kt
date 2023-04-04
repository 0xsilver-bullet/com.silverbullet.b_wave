package com.silverbullet.core.events.server

import com.silverbullet.core.model.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(ReceivedMessageEvent.EventName)
data class ReceivedMessageEvent(
    val message: Message
): ServerEvent{

    companion object{

        const val EventName = "received_message_s_event"
    }
}
