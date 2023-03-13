package com.silverbullet.core.events.server

import com.silverbullet.core.model.DmMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event is sent to user (sender) when he sends a dm message and it's sent successfully .
 * the provisional id is included in case the sender attached a provisional id to the send dm event.
 */
@Serializable
@SerialName(DmSentEvent.EventName)
data class DmSentEvent(
    val message: DmMessage,
    val provisionalId: String?
) : ServerEvent {

    companion object {

        const val EventName = "dm_sent_event_s_event"
    }
}
