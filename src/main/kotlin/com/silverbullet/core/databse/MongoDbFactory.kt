package com.silverbullet.core.databse

import com.mongodb.client.model.IndexOptions
import com.silverbullet.core.databse.entity.MessageEntity
import com.silverbullet.core.databse.entity.RefreshTokenEntity
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object MongoDbFactory {

    const val dbname = "b_wave_db"

    /**
     * set up collections indexes
     */
    suspend fun init() {
        val database = KMongo
            .createClient()
            .getDatabase(dbname)
            .coroutine

        val tokenCollection = database.getCollection<RefreshTokenEntity>()
        val messagesCollection = database.getCollection<MessageEntity>()

        tokenCollection
            .ensureIndex(
                RefreshTokenEntity::userId,
                indexOptions = IndexOptions().unique(true)
            )

        messagesCollection
            .ensureIndex(MessageEntity::channelId)

    }
}