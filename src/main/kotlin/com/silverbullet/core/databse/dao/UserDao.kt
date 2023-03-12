package com.silverbullet.core.databse.dao

import com.silverbullet.core.databse.DatabaseFactory.dbQuery
import com.silverbullet.core.databse.entity.UserEntity
import com.silverbullet.core.databse.table.UsersTable
import com.silverbullet.core.databse.utils.DbError
import com.silverbullet.core.databse.utils.DbOperation
import com.silverbullet.core.databse.utils.PSQLErrorCodes
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select

interface UserDao {

    suspend fun insertUser(
        name: String,
        username: String,
        profilePicUrl: String?,
        password: String,
        salt: String
    ): DbOperation<UserEntity>

    /**
     * @return user entity if it's found
     */
    suspend fun getUserByUsername(username: String): DbOperation.Success<UserEntity?>

    suspend fun getUserById(userId: Int): DbOperation.Success<UserEntity?>

    suspend fun getUsersByIds(userIds: List<Int>): DbOperation.Success<List<UserEntity>>
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

    override suspend fun getUserByUsername(username: String): DbOperation.Success<UserEntity?> =
        dbQuery {
            val user = UsersTable
                .select { UsersTable.username eq username }
                .singleOrNull()
                ?.toUserEntity()
            DbOperation.Success(user)
        }

    override suspend fun getUserById(userId: Int): DbOperation.Success<UserEntity?> =
        dbQuery {
            val user = UsersTable
                .select { UsersTable.id eq userId }
                .singleOrNull()
                ?.toUserEntity()
            DbOperation.Success(user)
        }

    override suspend fun getUsersByIds(userIds: List<Int>): DbOperation.Success<List<UserEntity>> =
        dbQuery {
            val users = UsersTable
                .select { UsersTable.id inList userIds }
                .map { it.toUserEntity() }
            DbOperation.Success(users)
        }

    private fun ResultRow.toUserEntity(): UserEntity = UserEntity(
        id = this[UsersTable.id].value,
        name = this[UsersTable.name],
        username = this[UsersTable.username],
        profilePicUrl = this[UsersTable.profilePicUrl],
        password = this[UsersTable.password],
        salt = this[UsersTable.salt]
    )
}