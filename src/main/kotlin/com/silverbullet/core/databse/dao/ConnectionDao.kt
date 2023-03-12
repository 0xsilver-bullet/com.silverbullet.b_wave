package com.silverbullet.core.databse.dao

import com.silverbullet.core.databse.DatabaseFactory.dbQuery
import com.silverbullet.core.databse.table.ConnectionsTable
import com.silverbullet.core.databse.utils.DbError
import com.silverbullet.core.databse.utils.DbOperation
import com.silverbullet.core.databse.utils.PSQLErrorCodes
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select

interface ConnectionDao {
    suspend fun createUserConnection(
        user1Id: Int,
        user2Id: Int
    ): DbOperation<Unit>

    /**
     * @return a list with the users ids that this user is connected with.
     */
    suspend fun getUserConnections(userId: Int): DbOperation.Success<List<Int>>
}

class ConnectionDaoImpl : ConnectionDao {

    override suspend fun createUserConnection(user1Id: Int, user2Id: Int): DbOperation<Unit> =
        dbQuery {
            try {
                ConnectionsTable.insert {
                    it[u1] = user1Id
                    it[u2] = user2Id
                }
                DbOperation.Success(Unit)
            } catch (e: ExposedSQLException) {
                val dbError = when (e.sqlState) {
                    PSQLErrorCodes.duplicateKey -> DbError.DuplicateKey("") // TODO: maybe key name should be nullable
                    PSQLErrorCodes.violatedConstraints -> DbError.ViolatedConstraint(null)
                    else -> DbError.UnknownError
                }
                DbOperation.Failure(dbError)
            } catch (e: Exception) {
                DbOperation.Failure(DbError.UnknownError)
            }
        }

    override suspend fun getUserConnections(userId: Int): DbOperation.Success<List<Int>> =
        dbQuery {
            try {
                val connections = ConnectionsTable
                    .select { (ConnectionsTable.u1 eq userId) or (ConnectionsTable.u2 eq userId) }
                    .map { resultRow ->
                        // extract the user ids which are not eq to userId because those are the user connections
                        if (resultRow[ConnectionsTable.u1] == userId)
                            resultRow[ConnectionsTable.u2]
                        else
                            resultRow[ConnectionsTable.u1]
                    }
                DbOperation.Success(connections)
            } catch (e: Exception) {
                DbOperation.Success(emptyList())
            }
        }
}