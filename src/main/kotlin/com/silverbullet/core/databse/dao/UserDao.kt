package com.silverbullet.core.databse.dao

import com.silverbullet.core.databse.DatabaseFactory.dbQuery
import com.silverbullet.core.databse.entity.UserEntity
import com.silverbullet.core.databse.table.UsersTable
import com.silverbullet.core.databse.utils.DbError
import com.silverbullet.core.databse.utils.DbOperation
import com.silverbullet.core.databse.utils.PSQLErrorCodes
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insertAndGetId

interface UserDao {

    suspend fun insertUser(
        name: String,
        username: String,
        profilePicUrl: String?,
        password: String,
        salt: String
    ): DbOperation<UserEntity>
}

class UserDaoImpl : UserDao {

    override suspend fun insertUser(
        name: String,
        username: String,
        profilePicUrl: String?,
        password: String,
        salt: String
    ): DbOperation<UserEntity> =
        dbQuery {
            try {
                val userId = UsersTable.insertAndGetId {
                    it[UsersTable.name] = name
                    it[UsersTable.username] = username
                    it[UsersTable.profilePicUrl] = profilePicUrl
                    it[UsersTable.password] = password
                    it[UsersTable.salt] = salt
                }
                val userEntity = UserEntity(
                    id = userId.value,
                    name = name,
                    username = username,
                    profilePicUrl = profilePicUrl,
                    password = password,
                    salt = salt
                )
                DbOperation.Success(userEntity)
            } catch (error: ExposedSQLException) {
                val dbError = when (error.sqlState) {
                    PSQLErrorCodes.duplicateKey -> DbError.DuplicateKey("username")
                    else -> DbError.UnknownError
                }
                DbOperation.Failure(dbError)
            } catch (e: Exception) {
                DbOperation.Failure(DbError.UnknownError)
            }
        }
}