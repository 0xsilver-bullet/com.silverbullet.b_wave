package com.silverbullet.feature_auth.route

import com.silverbullet.feature_auth.AuthController
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.configureAuthRoutes(){

    val authController by inject<AuthController>()

    route("auth"){

        signupRoute(controller = authController)
        loginRoute(controller = authController)
        refreshToken(controller = authController)
    }
}