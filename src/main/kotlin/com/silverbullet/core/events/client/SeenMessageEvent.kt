package com.silverbullet.core.events.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(SeenMessageEvent.EventName)
data class SeenMessageEvent(
    val messageId: String
): ClientEvent {

    companion object {

        const val EventName = "seen_message_cli_event"
    }
}
