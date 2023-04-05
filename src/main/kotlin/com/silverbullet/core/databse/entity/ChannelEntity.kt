package com.silverbullet.core.databse.entity

import com.silverbullet.core.databse.utils.ChannelType

data class ChannelEntity(
    val id: Int,
    val type: ChannelType,
    val name: String?,
    val users: List<UserEntity>
)
