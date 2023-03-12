package com.silverbullet.feature_auth.route

import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_auth.AuthController
import com.silverbullet.utils.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.logout(controller: AuthController) {

    authenticate {

        post("logout") {

            val userId = call.userId ?: throw UnexpectedServiceError()

            controller.processLogoutRequest(userId)

            call.response.status(HttpStatusCode.OK)
        }
    }
}