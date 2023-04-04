package com.silverbullet.feature_connection.route

import com.silverbullet.feature_connection.ConnectionsController
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.configureConnectionsRoutes() {

    val connectionsController by inject<ConnectionsController>()

    route("connection") {

        connectRoute(controller = connectionsController)
        userConnections(controller = connectionsController)
        generateFriendshipSecret(controller = connectionsController)
    }
}