package com.silverbullet.core.events

import com.silverbullet.core.databse.dao.DmMessageDao
import com.silverbullet.core.databse.entity.DmMessageEntity
import com.silverbullet.core.events.client.ClientEvent
import com.silverbullet.core.events.client.SendDmMessageEvent
import com.silverbullet.core.events.server.ReceivedDmMessageEvent
import com.silverbullet.core.events.server.ServerEvent
import com.silverbullet.core.mapper.toDmMessage
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
    private val dmsDao: DmMessageDao
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
            is SendDmMessageEvent -> handleSendDmMessageEvent(userId, event)
        }
    }

    private suspend fun handleSendDmMessageEvent(
        userId: Int,
        event: SendDmMessageEvent
    ) {
        val dmMessageEntity = DmMessageEntity
            .create(
                text = event.text,
                senderId = userId,
                receiverId = event.receiverId
            )

        dmsDao.insert(dmMessageEntity)

        // notify the receiver user
        val receivedMessageServerEvent = ReceivedDmMessageEvent(message = dmMessageEntity.toDmMessage())
        sendServerEvent(userId = dmMessageEntity.senderId, event = receivedMessageServerEvent)
        sendServerEvent(userId = dmMessageEntity.receiverId, event = receivedMessageServerEvent)
    }

}