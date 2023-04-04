package com.silverbullet.core.events.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(SendMessageEvent.EventName)
data class SendMessageEvent(
    val channelId: Int,
    val text: String? = null,
    val imageUrl: String? = null,
    val provisionalId: String? = null
) : ClientEvent {

    companion object {

        const val EventName = "send_message_cli_event"
    }
}
