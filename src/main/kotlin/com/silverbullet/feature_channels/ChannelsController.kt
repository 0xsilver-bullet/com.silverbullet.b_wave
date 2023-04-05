package com.silverbullet.feature_channels

import com.silverbullet.core.databse.dao.ChannelDao
import com.silverbullet.core.databse.entity.UserEntity
import com.silverbullet.core.mapper.toUserInfo
import com.silverbullet.core.model.Channel

class ChannelsController(private val channelDao: ChannelDao) {

    suspend fun processGetUserChannelsRequest(userId: Int): List<Channel> {
        val channels = channelDao.getUserChannels(userId).data
        return channels.map {
            Channel(
                id = it.id,
                name = it.name,
                type = it.type.ordinal,
                users = it.users.map(UserEntity::toUserInfo)
            )
        }
    }
}