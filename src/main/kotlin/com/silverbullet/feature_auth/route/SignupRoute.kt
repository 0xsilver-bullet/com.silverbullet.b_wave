package com.silverbullet.feature_auth.route

import com.silverbullet.core.utils.exceptions.InvalidRequestBody
import com.silverbullet.feature_auth.AuthController
import com.silverbullet.feature_auth.request.SignupRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signupRoute(controller: AuthController){

    post("signup") {

        val request = kotlin
            .runCatching { call.receiveNullable<SignupRequest>() }
            .getOrNull() ?: throw InvalidRequestBody()

        val response = controller.processSignupRequest(request)

        call.respond(response)
    }
}