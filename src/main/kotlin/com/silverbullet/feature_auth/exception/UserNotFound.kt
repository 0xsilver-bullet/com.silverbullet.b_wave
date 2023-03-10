package com.silverbullet.feature_auth.exception

import com.silverbullet.core.utils.exceptions.ServiceErrorCodes
import com.silverbullet.core.utils.exceptions.ServiceException
import io.ktor.http.*

class UserNotFound : ServiceException(
    httpStatusCode = HttpStatusCode.Conflict,
    errorCode = ServiceErrorCodes.UserNotFoundCode,
    errorText = "user not found"
)