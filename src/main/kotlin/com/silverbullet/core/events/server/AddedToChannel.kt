package com.silverbullet.core.events.server

import com.silverbullet.core.model.Channel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AddedToChannel.EventName)
data class AddedToChannel(
    val channel: Channel
): ServerEvent{

    companion object{

        const val EventName = "added_to_channel_s_event"
    }
}
