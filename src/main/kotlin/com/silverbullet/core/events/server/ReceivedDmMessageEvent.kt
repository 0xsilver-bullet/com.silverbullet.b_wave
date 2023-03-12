package com.silverbullet.core.events.server

import com.silverbullet.core.model.DmMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(ReceivedDmMessageEvent.EventName)
data class ReceivedDmMessageEvent(
    val message: DmMessage
): ServerEvent{

    companion object{

        const val EventName = "received_message_s_event"
    }
}
