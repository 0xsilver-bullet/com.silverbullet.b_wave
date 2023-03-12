package com.silverbullet.core.databse.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ConnectionsTable : Table() {

    val u1 = integer("u1")
        .references(
            UsersTable.id,
            onDelete = ReferenceOption.CASCADE
        )

    val u2 = integer("u2")
        .references(
            UsersTable.id,
            onDelete = ReferenceOption.CASCADE
        )

    override val primaryKey: PrimaryKey = PrimaryKey(u1, u2)

    init {
        // to prevent inserting the same connection twice, we always enter the smaller id first
        check("smaller_user_id_first") { u1 less  u2 }
    }
}