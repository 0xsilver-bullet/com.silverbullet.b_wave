package com.silverbullet.feature_messages.route

import com.silverbullet.feature_messages.MessageController
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.configureMessageRoutes(){

    val messagesController by inject<MessageController>()

    getChannelMessages(controller = messagesController)
}