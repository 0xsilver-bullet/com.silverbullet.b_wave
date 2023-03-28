package com.silverbullet.feature_profile.request

import kotlinx.serialization.Serializable

@Serializable
data class EditProfileRequest(
    val name: String? = null
)
