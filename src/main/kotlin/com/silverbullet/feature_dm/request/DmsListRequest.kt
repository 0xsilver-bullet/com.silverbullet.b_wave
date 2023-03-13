package com.silverbullet.feature_dm.request

import kotlinx.serialization.Serializable

@Serializable
data class DmsListRequest(
    val friendId: Int
)
