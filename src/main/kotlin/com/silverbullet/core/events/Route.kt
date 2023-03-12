package com.silverbullet.core.events

import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.utils.userId
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.ext.inject

fun Route.configureEventsRoute() {

    val eventsEngine by inject<EventsEngine>()

    authenticate {

        webSocket("events") {

            val userId = call.userId ?: throw UnexpectedServiceError()

            eventsEngine.handleClient(
                userId = userId,
                session = this
            )
        }
    }
}