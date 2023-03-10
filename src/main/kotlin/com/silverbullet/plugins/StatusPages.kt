package com.silverbullet.plugins

import com.silverbullet.core.utils.exceptions.ServiceException
import com.silverbullet.core.utils.exceptions.handleServiceException
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureStatusPages(){

    install(StatusPages) {

        exception<ServiceException> { call, serviceException ->

            call.handleServiceException(serviceException)
        }

    }

}