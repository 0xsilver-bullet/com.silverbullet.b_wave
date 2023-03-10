package com.silverbullet.core.utils.exceptions

import io.ktor.http.*

class InvalidRequestBody: ServiceException(
    httpStatusCode = HttpStatusCode.BadRequest,
    errorCode = ServiceErrorCodes.InvalidRequestBodyCode
)

class UnexpectedServiceError: ServiceException(
    httpStatusCode = HttpStatusCode.InternalServerError,
    errorCode = ServiceErrorCodes.UnexpectedServiceErrorCode
)