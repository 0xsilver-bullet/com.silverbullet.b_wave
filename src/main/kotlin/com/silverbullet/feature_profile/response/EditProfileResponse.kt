package com.silverbullet.feature_profile.response

import com.silverbullet.core.model.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class EditProfileResponse(
    val userInfo: UserInfo
)
