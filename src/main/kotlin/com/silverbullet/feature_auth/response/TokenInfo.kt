package com.silverbullet.feature_auth.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenInfo(
    val accessToken: String,
    val refreshToken: String
)
