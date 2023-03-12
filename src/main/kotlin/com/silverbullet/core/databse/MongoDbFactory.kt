package com.silverbullet.core.databse

import com.mongodb.client.model.IndexOptions
import com.silverbullet.core.databse.entity.RefreshTokenEntity
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object MongoDbFactory {

    const val dbname = "b_wave_db"

    /**
     * currently just sets the unique index for this collection
     */
    suspend fun init(){
        KMongo
            .createClient()
            .getDatabase(dbname)
            .coroutine
            .getCollection<RefreshTokenEntity>()
            .ensureIndex(
                RefreshTokenEntity::userId,
                indexOptions = IndexOptions().unique(true)
            )
    }
}