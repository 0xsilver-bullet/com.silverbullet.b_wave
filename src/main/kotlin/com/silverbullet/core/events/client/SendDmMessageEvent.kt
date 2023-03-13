package com.silverbullet.core.events.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(SendDmMessageEvent.EventName)
data class SendDmMessageEvent(
    val text: String,
    val receiverId: Int,
    val provisionalId: String? = null
) : ClientEvent {

    companion object {

        const val EventName = "send_dm_message_cli_event"
    }
}
