package com.silverbullet.core.databse.dao

import com.silverbullet.core.databse.entity.RefreshTokenEntity
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

interface RefreshTokenDao {

    suspend fun insertRefreshToken(
        token: RefreshTokenEntity
    )

    suspend fun getTokenByUserId(userId: Int): RefreshTokenEntity?

    /**
     * @return true if deleted ,false if it's not found (or unexpected behavior)
     */
    suspend fun deleteTokenByUserId(userId: Int): Boolean

    /**
     * @return indicate if it's successful or not.
     */
    suspend fun updateUserRefreshToken(userId: Int, newRefreshToken: String): Boolean
}

class RefreshTokenDaoImpl(
    db: CoroutineDatabase
) : RefreshTokenDao {

    private val collection = db.getCollection<RefreshTokenEntity>()

    override suspend fun insertRefreshToken(token: RefreshTokenEntity) {
        collection.insertOne(token)
    }

    override suspend fun getTokenByUserId(userId: Int): RefreshTokenEntity? {
        return collection
            .findOne(RefreshTokenEntity::userId eq userId)
    }

    override suspend fun deleteTokenByUserId(userId: Int): Boolean {
        return collection
            .deleteOne(RefreshTokenEntity::userId eq userId)
            .deletedCount == 1L
    }

    override suspend fun updateUserRefreshToken(userId: Int, newRefreshToken: String): Boolean {
        return collection
            .updateOne(
                filter = RefreshTokenEntity::userId eq userId,
                update = setValue(RefreshTokenEntity::token, newRefreshToken)
            )
            .wasAcknowledged()
    }
}