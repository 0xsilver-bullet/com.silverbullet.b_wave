package com.silverbullet.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    val name: String?,
    val id: Int
)
