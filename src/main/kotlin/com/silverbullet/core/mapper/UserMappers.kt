package com.silverbullet.core.mapper

import com.silverbullet.core.databse.entity.UserEntity
import com.silverbullet.core.model.UserInfo

fun UserEntity.toUserInfo(): UserInfo =
    UserInfo(
        name = name,
        username = username,
        profilePicUrl = profilePicUrl,
        id = id
    )