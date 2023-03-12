package com.silverbullet.feature_connection.route

import com.silverbullet.core.utils.exceptions.InvalidRequestBody
import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_connection.ConnectionsController
import com.silverbullet.feature_connection.request.ConnectRequest
import com.silverbullet.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.connectRoute(controller: ConnectionsController) {

    authenticate {

        post("connect") {

            val userId = call.userId ?: throw UnexpectedServiceError()

            val request = kotlin
                .runCatching { call.receiveNullable<ConnectRequest>() }
                .getOrNull() ?: throw InvalidRequestBody()

            val response = controller.processConnectRequest(request, userId)

            call.respond(response)
        }
    }
}