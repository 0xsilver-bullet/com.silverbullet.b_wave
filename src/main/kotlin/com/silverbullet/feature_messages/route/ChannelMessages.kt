package com.silverbullet.feature_messages.route

import com.silverbullet.feature_messages.FeatureMessages
import com.silverbullet.feature_messages.MessageController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getChannelMessages(controller: MessageController) {

    authenticate {

        get {

            val channelId = call.parameters[FeatureMessages.QueryParameters.channelId]
                ?.toIntOrNull()
                ?: kotlin.run {
                    call.respond(status = HttpStatusCode.BadRequest, message = "channel_id query param is required")
                    return@get
                }

            val messages = controller.getChannelMessages(channelId)

            call.respond(messages)
        }
    }
}