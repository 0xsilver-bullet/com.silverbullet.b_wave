package com.silverbullet.core.events.server

import com.silverbullet.core.model.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(UpdateMessagesEvent.EventName)
data class UpdateMessagesEvent(
    val messages: List<Message>
) : ServerEvent {

    companion object {

        const val EventName = "update_messages_s_event"
    }
}
