package com.silverbullet.core.databse.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

/**
 * This table is used to create friendship secrets for users, It's used to enable users to add each other so
 * fast and in a secure way where a user can request a secret for him and this secret will only be usable for a short time,
 * the user can share this secret with his friend in order to add him as a friend.
 */
object FriendshipSecretTable: Table() {

    val secret = varchar("secret", 128)
    val expirationDate = long("expiration_date")
    val user = integer("user_id")
        .references(
            UsersTable.id,
            onDelete = ReferenceOption.CASCADE
        )

    override val primaryKey = PrimaryKey(secret)
}