package com.silverbullet.feature_dm.route

import com.silverbullet.core.utils.exceptions.InvalidRequestBody
import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_dm.DmController
import com.silverbullet.feature_dm.request.DmsListRequest
import com.silverbullet.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.allDms(controller: DmController) {

    authenticate {

        get("all") {

            val request = kotlin
                .runCatching { call.receiveNullable<DmsListRequest>() }
                .getOrNull() ?: throw InvalidRequestBody()

            val userId = call.userId ?: throw UnexpectedServiceError()

            val dmMessages = controller.getUsersDm(userId, request.friendId)

            call.respond(dmMessages)
        }
    }
}