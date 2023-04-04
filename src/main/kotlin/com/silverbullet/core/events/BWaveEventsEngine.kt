package com.silverbullet.core.events

import com.silverbullet.core.databse.dao.ChannelDao
import com.silverbullet.core.databse.dao.ConnectionDao
import com.silverbullet.core.databse.dao.MessageDao
import com.silverbullet.core.databse.entity.MessageEntity
import com.silverbullet.core.events.client.ClientEvent
import com.silverbullet.core.events.client.SeenMessagesEvent
import com.silverbullet.core.events.client.SendMessageEvent
import com.silverbullet.core.events.server.*
import com.silverbullet.core.mapper.toMessage
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class BWaveEventsEngine(
    private val serializer: Json,
    private val connectionDao: ConnectionDao,
    private val channelDao: ChannelDao,
    private val messageDao: MessageDao
) : EventsEngine {

    private val connections = ConcurrentHashMap<Int, DefaultWebSocketServerSession>()

    override suspend fun handleClient(
        userId: Int,
        session: DefaultWebSocketServerSession
    ) {
        // prevent user from having multiple connections
        if (connections.containsKey(userId)) {
            session.close(
                reason = CloseReason(
                    code = CloseReason.Codes.VIOLATED_POLICY,
                    message = "You have more than connection"
                )
            )
            return
        }
        connections[userId] = session

        onUserConnected(userId)

        try {
            for (frame in session.incoming) {
                val eventJson = (frame as? Frame.Text)?.readText() ?: continue
                val event = try {
                    serializer.decodeFromString<ClientEvent>(eventJson)
                } catch (e: Exception) {
                    continue
                }
                // handle the client event in another coroutine and go back to watch for other events.
                coroutineScope { launch { handleUserEvent(userId, event) } }
            }
        } finally {
            connections.remove(userId)
            notifyFriendsWithOfflineStatus(userId)
        }
    }

    override suspend fun sendServerEvent(userId: Int, event: ServerEvent) {
        val userSession = connections[userId] ?: return
        val eventJson = serializer.encodeToString(event)
        try {
            userSession.send(Frame.Text(eventJson))
        } catch (e: ClosedReceiveChannelException) {
            connections.remove(userId)
        }
    }

    private suspend fun handleUserEvent(userId: Int, event: ClientEvent) {
        when (event) {
            is SendMessageEvent -> handleSendMessageEvent(userId, event)
            is SeenMessagesEvent -> handleSeenMessageEvent(userId, event)
        }
    }

    private suspend fun handleSendMessageEvent(
        userId: Int,
        event: SendMessageEvent
    ) {
        val messageEntity = MessageEntity(
            text = event.text,
            imageUrl = event.imageUrl,
            senderId = userId,
            channelId = event.channelId
        )

        messageDao.insert(messageEntity)

        val message = messageEntity.toMessage()

        // notify the receiver user
        val receivedMessageServerEvent = ReceivedMessageEvent(message = message)
        val channelMembers = channelDao.getChannelMembersIds(event.channelId).data
        channelMembers.forEach { memberId ->
            if (memberId != userId) {
                sendServerEvent(userId = memberId, event = receivedMessageServerEvent)
            }
        }

        // notify sender that message is sent
        val messageSentEvent = MessageSentEvent(
            message = message,
            provisionalId = event.provisionalId
        )
        sendServerEvent(userId = userId, event = messageSentEvent)
    }

    private suspend fun handleSeenMessageEvent(
        userId: Int,
        event: SeenMessagesEvent
    ) {
        val updatedMessages = messageDao
            .markMessagesAsSeen(
                channelId = event.channelId,
                messagesIds = event.messagesIds,
                userId = userId
            )

        // now we should notify the channel users with the updated messages.
        val updateMessagesEvent = UpdateMessagesEvent(updatedMessages.map(MessageEntity::toMessage))

        val channelUsers = channelDao.getChannelMembersIds(event.channelId).data
        channelUsers.forEach { channelUserId ->
            if (channelUserId != userId) {
                sendServerEvent(channelUserId, updateMessagesEvent)
            }
        }
    }

    /**
     * sends user default events he should receive once he's connected
     */
    private suspend fun onUserConnected(userId: Int) = coroutineScope {
        launch {
            // get all user friends and notify about them.
            val userConnections = connectionDao.getUserConnections(userId).data
            userConnections.forEach { friendId ->
                if (connections[friendId] != null) {
                    // notify the new connected user that his friend is online
                    val event1 = FriendOnlineStatusEvent(friendId = friendId, online = true)
                    sendServerEvent(userId, event1)

                    // notify his friend
                    val event2 = FriendOnlineStatusEvent(friendId = userId, online = true)
                    sendServerEvent(friendId, event2)
                }
            }
        }
    }

    private suspend fun notifyFriendsWithOfflineStatus(offlineUserId: Int) = coroutineScope {
        launch {
            val userFriends = connectionDao.getUserConnections(offlineUserId).data
            val offlineEvent = FriendOnlineStatusEvent(offlineUserId, online = false)
            userFriends.forEach { friendId ->
                sendServerEvent(friendId, offlineEvent)
            }
        }
    }

}