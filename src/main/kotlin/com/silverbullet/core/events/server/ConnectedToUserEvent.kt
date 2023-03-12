package com.silverbullet.core.events.server

import com.silverbullet.core.model.UserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(ConnectedToUserEvent.EventName)
data class ConnectedToUserEvent(
    val user: UserInfo
) : ServerEvent {

    companion object {

        const val EventName = "connected_to_user_s_event"
    }
}