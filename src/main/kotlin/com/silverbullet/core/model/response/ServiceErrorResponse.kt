package com.silverbullet.core.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ServiceErrorResponse(
    val errorCode: Int,
    val message: String? = null
)
