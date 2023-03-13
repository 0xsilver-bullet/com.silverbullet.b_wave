package com.silverbullet.core.events.server

import com.silverbullet.core.model.DmMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(UpdateDmMessageEvent.EventName)
data class UpdateDmMessageEvent(
    val message: DmMessage
) : ServerEvent {

    companion object {

        const val EventName = "update_dm_message_s_event"
    }
}
