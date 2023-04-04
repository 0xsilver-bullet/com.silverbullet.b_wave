package com.silverbullet.feature_connection

import com.silverbullet.core.databse.dao.ChannelDao
import com.silverbullet.core.databse.dao.ConnectionDao
import com.silverbullet.core.databse.dao.FriendshipSecretDao
import com.silverbullet.core.databse.dao.UserDao
import com.silverbullet.core.databse.utils.DbError
import com.silverbullet.core.databse.utils.DbOperation
import com.silverbullet.core.events.EventsEngine
import com.silverbullet.core.events.server.AddedToChannel
import com.silverbullet.core.events.server.ConnectedToUserEvent
import com.silverbullet.core.mapper.toUserInfo
import com.silverbullet.core.model.Channel
import com.silverbullet.core.model.UserInfo
import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_connection.exception.AlreadyConnectedUsers
import com.silverbullet.feature_connection.exception.FriendshipSecretExpired
import com.silverbullet.feature_connection.exception.InvalidFriendshipSecret
import com.silverbullet.feature_connection.request.ConnectRequest
import com.silverbullet.feature_connection.response.ConnectResponse
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ConnectionsController(
    private val eventsEngine: EventsEngine,
    private val userDao: UserDao,
    private val connectionDao: ConnectionDao,
    private val channelDao: ChannelDao,
    private val friendshipSecretDao: FriendshipSecretDao
) {

    suspend fun processConnectRequest(
        request: ConnectRequest,
        requesterId: Int
    ): ConnectResponse {
        val friendshipSecret =
            friendshipSecretDao
                .getFriendshipSecret(request.secret).data ?: throw InvalidFriendshipSecret()

        if (System.currentTimeMillis() > friendshipSecret.expirationDate) {
            // then this secret is expired
            throw FriendshipSecretExpired()
        }

        val connectionResult = connectionDao
            .createUserConnection(
                requesterId.coerceAtMost(friendshipSecret.user.id), // just to ensure smaller id is passed first
                friendshipSecret.user.id.coerceAtLeast(requesterId)
            )
        if (connectionResult is DbOperation.Success) {
            notifyUserAboutConnection(notifiedUser = friendshipSecret.user.id, userId = requesterId)
            createDmChannel(friendshipSecret.user.id, requesterId)
            return ConnectResponse(connectedUser = friendshipSecret.user.toUserInfo())
        }
        // Then connecting failed
        when ((connectionResult as DbOperation.Failure).error) {
            is DbError.DuplicateKey -> throw AlreadyConnectedUsers()
            else -> throw UnexpectedServiceError()
        }
    }

    suspend fun processGetUserConnectionsRequest(userId: Int): List<UserInfo> {
        val userConnections = connectionDao.getUserConnections(userId).data
        return userDao
            .getUsersByIds(userConnections)
            .data
            .map { it.toUserInfo() }
    }

    suspend fun generateFriendshipSecret(userId: Int): String {
        // TODO: Maybe you want the expiration date dynamic instead of always 1 minute
        val expirationDate = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1)
        return friendshipSecretDao.createFriendshipSecret(userId, expirationDate).data
    }

    /**
     * @param notifiedUser user who should be notified.
     * @param userId user who started connection.
     */
    private suspend fun notifyUserAboutConnection(
        notifiedUser: Int,
        userId: Int
    ) = coroutineScope {
        launch {
            val userInfo = userDao.getUserById(userId).data?.toUserInfo() ?: return@launch
            val connectionEvent = ConnectedToUserEvent(userInfo)
            eventsEngine.sendServerEvent(userId = notifiedUser, event = connectionEvent)
        }
    }

    /**
     * Creates a DM channel for the users and notifies them about the channel
     */
    private suspend fun createDmChannel(user1Id: Int, user2Id: Int) = coroutineScope {
        launch {
            val dmChannelId = channelDao.createDmChannel().data
            val isUser1Added = channelDao.createChannelMembership(dmChannelId, user1Id) is DbOperation.Success
            val isUser2Added = channelDao.createChannelMembership(dmChannelId, user2Id) is DbOperation.Success
            if (!isUser1Added && !isUser2Added) {
                return@launch
            }
            // DmChannel name is decided client side usually
            val channel = Channel(null, dmChannelId)
            val addedToChannelEvent = AddedToChannel(channel)

            // notify the users
            eventsEngine.sendServerEvent(user1Id, addedToChannelEvent)
            eventsEngine.sendServerEvent(user2Id, addedToChannelEvent)
        }
    }

}