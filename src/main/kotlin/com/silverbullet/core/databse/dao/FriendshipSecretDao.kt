package com.silverbullet.core.databse.dao

import com.silverbullet.core.databse.DatabaseFactory.dbQuery
import com.silverbullet.core.databse.entity.FriendshipSecretEntity
import com.silverbullet.core.databse.table.FriendshipSecretTable
import com.silverbullet.core.databse.table.UsersTable
import com.silverbullet.core.databse.utils.DbOperation
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

interface FriendshipSecretDao {

    /**
     * @param userId is the user referenced by the secret
     */
    suspend fun createFriendshipSecret(userId: Int, expirationDate: Long): DbOperation.Success<String>

    suspend fun getFriendshipSecret(secret: String): DbOperation.Success<FriendshipSecretEntity?>
}

class FriendshipSecretDaoImpl : FriendshipSecretDao {

    override suspend fun createFriendshipSecret(userId: Int, expirationDate: Long): DbOperation.Success<String> =
        dbQuery {
            val secret = UUID.randomUUID().toString()
            FriendshipSecretTable.insert {
                it[FriendshipSecretTable.secret] = secret
                it[user] = userId
                it[FriendshipSecretTable.expirationDate] = expirationDate
            }
            DbOperation.Success(secret)
        }

    override suspend fun getFriendshipSecret(secret: String): DbOperation.Success<FriendshipSecretEntity?> =
        dbQuery {
            val result = (FriendshipSecretTable innerJoin UsersTable)
                .select { FriendshipSecretTable.secret eq secret }
                .singleOrNull()
            val friendshipSecret = result?.let { FriendshipSecretEntity.fromResultRow(result) }
            DbOperation.Success(friendshipSecret)
        }
}