package com.silverbullet.feature_connection.route

import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_connection.ConnectionsController
import com.silverbullet.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userConnections(controller: ConnectionsController) {

    authenticate {

        get {

            val userId = call.userId ?: throw UnexpectedServiceError()

            val response = controller.processGetUserConnectionsRequest(userId)

            call.respond(response)
        }
    }
}