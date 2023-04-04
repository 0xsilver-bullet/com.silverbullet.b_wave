package com.silverbullet.core.databse.entity

import com.silverbullet.core.databse.table.UsersTable
import org.jetbrains.exposed.sql.ResultRow

data class UserEntity(
    val id: Int,
    val name: String,
    val username: String,
    val profilePicUrl: String?,
    val profilePicLocalPath: String?,
    val password: String,
    val salt: String
){

    companion object{

        fun fromResultRow(row: ResultRow): UserEntity = UserEntity(
            id = row[UsersTable.id].value,
            name = row[UsersTable.name],
            username = row[UsersTable.username],
            profilePicUrl = row[UsersTable.profilePicUrl],
            profilePicLocalPath = row[UsersTable.profilePicLocalPath],
            password = row[UsersTable.password],
            salt = row[UsersTable.salt]
        )
    }
}
