package com.silverbullet.core.databse.dao

import com.silverbullet.core.databse.DatabaseFactory.dbQuery
import com.silverbullet.core.databse.entity.ChannelEntity
import com.silverbullet.core.databse.entity.UserEntity
import com.silverbullet.core.databse.table.ChannelMembership
import com.silverbullet.core.databse.table.ChannelsTable
import com.silverbullet.core.databse.table.UsersTable
import com.silverbullet.core.databse.utils.ChannelType
import com.silverbullet.core.databse.utils.DbError
import com.silverbullet.core.databse.utils.DbOperation
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

interface ChannelDao {

    suspend fun createDmChannel(): DbOperation.Success<Int>

    suspend fun createGroupChannel(name: String): DbOperation.Success<Int>

    /**
     * adds a user to channel
     */
    suspend fun createChannelMembership(channelId: Int, userId: Int): DbOperation<Boolean>

    /**
     * Returns the ids of the members of a channel
     */
    suspend fun getChannelMembersIds(channelId: Int): DbOperation.Success<List<Int>>

    suspend fun getUserChannels(userId: Int): DbOperation.Success<List<ChannelEntity>>
}

class ChannelDaoImpl : ChannelDao {

    override suspend fun createDmChannel(): DbOperation.Success<Int> =
        dbQuery {
            val newChannelId = ChannelsTable.insertAndGetId {
                it[type] = ChannelType.DmChannel
            }.value
            DbOperation.Success(newChannelId)
        }

    override suspend fun createGroupChannel(name: String): DbOperation.Success<Int> =
        dbQuery {
            val newChannelId = ChannelsTable.insertAndGetId {
                it[ChannelsTable.name] = name
                it[type] = ChannelType.GroupChannel
            }.value
            DbOperation.Success(newChannelId)
        }

    override suspend fun createChannelMembership(channelId: Int, userId: Int): DbOperation<Boolean> =
        dbQuery {
            try {
                ChannelMembership.insert {
                    it[ChannelMembership.channelId] = channelId
                    it[memberId] = userId
                }
                DbOperation.Success(true)
            } catch (error: ExposedSQLException) {
                DbOperation.Failure(DbError.UnknownError)
            }
        }

    override suspend fun getChannelMembersIds(channelId: Int): DbOperation.Success<List<Int>> =
        dbQuery {
            val channelMembers = ChannelMembership
                .slice(ChannelMembership.memberId)
                .select { ChannelMembership.channelId eq channelId }
                .map { it[ChannelMembership.memberId] }
            DbOperation.Success(channelMembers)
        }

    override suspend fun getUserChannels(userId: Int): DbOperation.Success<List<ChannelEntity>> =
        dbQuery {
            val result = Join(
                table = ChannelMembership,
                otherTable = ChannelsTable,
                joinType = JoinType.INNER,
                onColumn = ChannelMembership.channelId,
                otherColumn = ChannelsTable.id
            ).join(
                otherTable = UsersTable,
                joinType = JoinType.INNER,
                onColumn = ChannelMembership.memberId,
                otherColumn = UsersTable.id
            )
                .select {
                    ChannelMembership.channelId inSubQuery ChannelMembership
                        .slice(ChannelMembership.channelId)
                        .select { ChannelMembership.memberId eq userId }
                }.groupBy {
                    it[ChannelMembership.channelId]
                }

            val channels = result.map { map ->
                val users = map.value.map { row -> UserEntity.fromResultRow(row) }
                ChannelEntity(
                    id = map.key,
                    type = map.value[0][ChannelsTable.type],
                    name = map.value[0][ChannelsTable.name],
                    users = users
                )
            }

            DbOperation.Success(channels)
        }
}