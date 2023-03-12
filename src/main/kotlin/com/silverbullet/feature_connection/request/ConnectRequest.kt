package com.silverbullet.feature_connection.request

import kotlinx.serialization.Serializable

/**
 * @param username the username the sender user wants to connect to
 */
@Serializable
data class ConnectRequest(
    val username: String
)
