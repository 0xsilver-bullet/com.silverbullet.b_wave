package com.silverbullet.feature_channels.route

import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_channels.ChannelsController
import com.silverbullet.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userChannels(controller: ChannelsController) {

    authenticate {

        get {

            val userId = call.userId ?: throw UnexpectedServiceError()

            val channels = controller.processGetUserChannelsRequest(userId)

            call.respond(mapOf("channels" to channels))
        }
    }

}