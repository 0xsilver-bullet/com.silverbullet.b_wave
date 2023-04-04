package com.silverbullet.feature_connection.request

import kotlinx.serialization.Serializable

/**
 * @param secret friendship secret of the other user the client wants to be friend with
 */
@Serializable
data class ConnectRequest(
    val secret: String
)
