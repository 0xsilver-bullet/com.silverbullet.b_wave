package com.silverbullet.core.databse.dao

import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.silverbullet.core.databse.entity.MessageEntity
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

interface MessageDao {

    suspend fun insert(message: MessageEntity)

    suspend fun delete(messageId: String)

    suspend fun getChannelMessages(channelId: Int): List<MessageEntity>

    /**
     * @param userId the user who had seen the message.
     * @return In case the message is updated it will return the new version, if not it will return null.
     */
    suspend fun markMessageAsSeen(messageId: String, userId: Int): MessageEntity?
}

class MessageDaoImpl(db: CoroutineDatabase) : MessageDao {

    private val collection = db.getCollection<MessageEntity>()

    override suspend fun insert(message: MessageEntity) {
        collection.insertOne(message)
    }

    override suspend fun delete(messageId: String) {
        collection.deleteOne(MessageEntity::id eq messageId)
    }

    override suspend fun getChannelMessages(channelId: Int): List<MessageEntity> {
        return collection
            .find(MessageEntity::channelId eq channelId)
            .descendingSort(MessageEntity::timestamp)
            .toList()
    }

    override suspend fun markMessageAsSeen(messageId: String, userId: Int): MessageEntity? {
        return collection
            .findOneAndUpdate(
                MessageEntity::id eq messageId,
                addToSet(MessageEntity::seenBy, userId),
                FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            )
    }
}