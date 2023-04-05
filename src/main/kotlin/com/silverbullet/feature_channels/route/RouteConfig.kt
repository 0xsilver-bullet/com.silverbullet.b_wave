package com.silverbullet.feature_channels.route

import com.silverbullet.feature_channels.ChannelsController
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.configureChannelsRoutes(){

    val channelsController by inject<ChannelsController>()

    route("channels"){

        userChannels(controller = channelsController)
    }
}