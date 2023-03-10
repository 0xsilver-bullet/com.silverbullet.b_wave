package com.silverbullet.plugins

import com.silverbullet.feature_auth.route.configureAuthRoutes
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    
    routing {

        configureAuthRoutes()
    }

}
