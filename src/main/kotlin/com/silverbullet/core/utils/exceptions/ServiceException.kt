package com.silverbullet.core.utils.exceptions

import com.silverbullet.core.model.response.ServiceErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

abstract class ServiceException(
    val httpStatusCode: HttpStatusCode,
    val errorCode: Int,
    errorText: String? = null
) : Exception(errorText)

suspend inline fun ApplicationCall.handleServiceException(serviceException: ServiceException) {
    val response =
        ServiceErrorResponse(
            errorCode = serviceException.errorCode,
            message = serviceException.message
        )
    respond(
        status = serviceException.httpStatusCode,
        message = response
    )
}