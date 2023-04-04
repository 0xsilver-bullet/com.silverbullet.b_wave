package com.silverbullet.feature_connection.route

import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_connection.ConnectionsController
import com.silverbullet.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.generateFriendshipSecret(controller: ConnectionsController) {

    authenticate {

        post("generate-secret") {

            val userId = call.userId ?: throw UnexpectedServiceError()

            val friendshipSecret = controller.generateFriendshipSecret(userId)

            call.respond(mapOf("secret" to friendshipSecret))
        }
    }
}