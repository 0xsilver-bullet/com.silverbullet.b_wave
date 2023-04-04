package com.silverbullet.core.events.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(SeenMessagesEvent.EventName)
data class SeenMessagesEvent(
    val channelId: Int,
    val messagesIds: List<String>
): ClientEvent {

    companion object {

        const val EventName = "seen_messages_cli_event"
    }
}
