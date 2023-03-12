package com.silverbullet.feature_auth.response

import com.silverbullet.core.model.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val user: UserInfo,
    val tokens: TokenInfo
)