package com.silverbullet.feature_profile.route

import com.silverbullet.feature_profile.ProfileController
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.configureProfileRoutes(){

    val profileController by inject<ProfileController>()

    route("profile"){

        editProfileRoute(controller = profileController)
    }
}