package com.silverbullet.core.databse.entity

import com.silverbullet.core.databse.table.FriendshipSecretTable
import org.jetbrains.exposed.sql.ResultRow

data class FriendshipSecretEntity(
    val secret: String,
    val expirationDate: Long,
    val user: UserEntity
) {

    companion object {

        fun fromResultRow(row: ResultRow): FriendshipSecretEntity {

            val secret = row[FriendshipSecretTable.secret]
            val expirationDate = row[FriendshipSecretTable.expirationDate]
            val userEntity = UserEntity.fromResultRow(row)

            return FriendshipSecretEntity(
                secret = secret,
                expirationDate = expirationDate,
                user = userEntity
            )
        }
    }
}
