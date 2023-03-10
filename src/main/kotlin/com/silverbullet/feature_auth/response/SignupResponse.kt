package com.silverbullet.feature_auth.response

import com.silverbullet.core.model.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    val user: UserInfo
)
