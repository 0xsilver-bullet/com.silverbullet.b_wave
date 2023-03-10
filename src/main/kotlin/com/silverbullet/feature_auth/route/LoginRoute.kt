package com.silverbullet.feature_auth.route

import com.silverbullet.core.utils.exceptions.InvalidRequestBody
import com.silverbullet.feature_auth.AuthController
import com.silverbullet.feature_auth.request.LoginRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.loginRoute(controller: AuthController) {

    post("login") {

        val request = kotlin
            .runCatching { call.receiveNullable<LoginRequest>() }
            .getOrNull() ?: throw InvalidRequestBody()

        val response = controller.processLoginRequest(request)

        call.respond(response)
    }
}