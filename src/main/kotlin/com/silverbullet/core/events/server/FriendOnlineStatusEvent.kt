package com.silverbullet.core.events.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(FriendOnlineStatusEvent.EventName)
data class FriendOnlineStatusEvent(
    val friendId: Int,
    val online: Boolean
) : ServerEvent {

    companion object {

        const val EventName = "friend_online_status_s_event"
    }
}
