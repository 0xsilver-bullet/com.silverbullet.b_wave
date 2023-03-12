package com.silverbullet.feature_auth.route

import com.silverbullet.core.utils.exceptions.InvalidRequestBody
import com.silverbullet.feature_auth.AuthController
import com.silverbullet.feature_auth.request.RefreshTokenRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.refreshToken(controller: AuthController) {

    post("refresh") {

        val request = kotlin
            .runCatching { call.receiveNullable<RefreshTokenRequest>() }
            .getOrNull() ?: throw InvalidRequestBody()

        val newTokens = controller.processRefreshTokenRequest(request)

        call.respond(newTokens)
    }
}