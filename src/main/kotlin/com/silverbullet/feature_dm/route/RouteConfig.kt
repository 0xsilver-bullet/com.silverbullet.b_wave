package com.silverbullet.feature_dm.route

import com.silverbullet.feature_dm.DmController
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.configureDmMessagesRoutes() {

    val dmController by inject<DmController>()

    route("dm") {

        allDms(controller = dmController)
    }
}