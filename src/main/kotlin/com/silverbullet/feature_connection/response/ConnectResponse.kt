package com.silverbullet.feature_connection.response

import com.silverbullet.core.model.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class ConnectResponse(
    val connectedUser: UserInfo
)
