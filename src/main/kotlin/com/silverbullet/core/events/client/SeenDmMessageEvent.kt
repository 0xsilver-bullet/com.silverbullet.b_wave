package com.silverbullet.core.events.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(SeenDmMessageEvent.EventName)
data class SeenDmMessageEvent(
    val messageId: String
): ClientEvent {

    companion object {

        const val EventName = "seen_dm_message_cli_event"
    }
}
