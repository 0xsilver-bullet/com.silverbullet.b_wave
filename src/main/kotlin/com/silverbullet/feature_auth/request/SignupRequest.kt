package com.silverbullet.feature_auth.request

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val name: String,
    val username: String,
    val password: String
)