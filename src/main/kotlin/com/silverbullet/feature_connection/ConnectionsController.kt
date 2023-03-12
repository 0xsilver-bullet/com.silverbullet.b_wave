package com.silverbullet.feature_connection

import com.silverbullet.core.databse.dao.ConnectionDao
import com.silverbullet.core.databse.dao.UserDao
import com.silverbullet.core.databse.utils.DbError
import com.silverbullet.core.databse.utils.DbOperation
import com.silverbullet.core.events.EventsEngine
import com.silverbullet.core.events.server.ConnectedToUserEvent
import com.silverbullet.core.mapper.toUserInfo
import com.silverbullet.core.model.UserInfo
import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_auth.exception.UserNotFound
import com.silverbullet.feature_connection.exception.AlreadyConnectedUsers
import com.silverbullet.feature_connection.request.ConnectRequest
import com.silverbullet.feature_connection.response.ConnectResponse
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ConnectionsController(
    private val userDao: UserDao,
    private val connectionDao: ConnectionDao,
    private val eventsEngine: EventsEngine
) {

    suspend fun processConnectRequest(
        request: ConnectRequest,
        requesterId: Int
    ): ConnectResponse {
        val targetUser = userDao
            .getUserByUsername(request.username)
            .data ?: throw UserNotFound()
        val connectionResult = connectionDao
            .createUserConnection(
                requesterId.coerceAtMost(targetUser.id), // just to ensure smaller id is passed first
                targetUser.id.coerceAtLeast(requesterId)
            )
        if (connectionResult is DbOperation.Success) {
            notifyUserAboutConnection(notifiedUser = targetUser.id, userId = requesterId)
            return ConnectResponse(connectedUser = targetUser.toUserInfo())
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

    /**
     * @param notifiedUser user who should be notified.
     * @param userId user who started connection.
     */
    private suspend fun notifyUserAboutConnection(
        notifiedUser: Int,
        userId: Int
    ) {
        coroutineScope {
            launch {
                val userInfo = userDao.getUserById(userId).data?.toUserInfo() ?: return@launch
                val connectionEvent = ConnectedToUserEvent(userInfo)
                eventsEngine.sendServerEvent(userId = notifiedUser, event = connectionEvent)
            }
        }
    }
}