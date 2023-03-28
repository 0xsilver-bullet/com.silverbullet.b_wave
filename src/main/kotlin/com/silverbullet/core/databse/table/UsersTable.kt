package com.silverbullet.core.databse.table

import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable : IntIdTable() {

    val name = varchar("name", 32)
    val username = varchar("username", 32).uniqueIndex()
    val profilePicUrl = varchar("profile_pic_url", 255).nullable()
    val profilePicLocalPath = varchar("profile_pic_local_path",255).nullable()
    val password = varchar("password", 256)
    val salt = varchar("salt",32)
}