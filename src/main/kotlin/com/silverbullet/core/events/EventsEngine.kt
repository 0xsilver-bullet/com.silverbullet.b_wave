package com.silverbullet.core.events

import com.silverbullet.core.events.server.ServerEvent
import io.ktor.server.websocket.*

interface EventsEngine {

    suspend fun handleClient(
        userId: Int,
        session: DefaultWebSocketServerSession
    )

    /**
     * looks for the client if he is connected and if he's, it will send an event to him.
     */
    suspend fun sendServerEvent(
        userId: Int,
        event: ServerEvent
    )
}