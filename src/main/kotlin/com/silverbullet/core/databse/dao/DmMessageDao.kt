package com.silverbullet.core.databse.dao

import com.silverbullet.core.databse.entity.DmMessageEntity
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or

interface DmMessageDao {

    suspend fun insert(dmMessage: DmMessageEntity)

    suspend fun delete(messageId: String)

    suspend fun getUsersChat(u1Id: Int, u2Id: Int): List<DmMessageEntity>
}

class DmMessageDaoImpl(db: CoroutineDatabase) : DmMessageDao {

    private val collection = db.getCollection<DmMessageEntity>()

    override suspend fun insert(dmMessage: DmMessageEntity) {
        collection.insertOne(dmMessage)
    }

    override suspend fun delete(messageId: String) {
        collection.deleteOne(DmMessageEntity::id eq messageId)
    }

    override suspend fun getUsersChat(u1Id: Int, u2Id: Int): List<DmMessageEntity> {
        return collection
            .find(
                or(
                    and(DmMessageEntity::senderId eq u1Id, DmMessageEntity::receiverId eq u2Id),
                    and(DmMessageEntity::receiverId eq u1Id, DmMessageEntity::senderId eq u2Id),
                )
            )
            .descendingSort(DmMessageEntity::timestamp)
            .toList()
    }
}