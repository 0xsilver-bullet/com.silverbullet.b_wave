package com.silverbullet.feature_dm

import com.silverbullet.core.databse.dao.DmMessageDao
import com.silverbullet.core.mapper.toDmMessage
import com.silverbullet.core.model.DmMessage

class DmController(private val dmMessageDao: DmMessageDao) {

    suspend fun getUsersDm(u1Id: Int, u2Id: Int): List<DmMessage> {
        return dmMessageDao.getUsersChat(u1Id,u2Id).map { it.toDmMessage() }
    }
}