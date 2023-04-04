package com.silverbullet.core.events.server

import com.silverbullet.core.model.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(UpdateMessageEvent.EventName)
data class UpdateMessageEvent(
    val message: Message
) : ServerEvent {

    companion object {

        const val EventName = "update_message_s_event"
    }
}
