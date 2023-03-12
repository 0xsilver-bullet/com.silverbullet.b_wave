package com.silverbullet.feature_connection.exception

import com.silverbullet.core.utils.exceptions.ServiceErrorCodes
import com.silverbullet.core.utils.exceptions.ServiceException
import io.ktor.http.*

class AlreadyConnectedUsers: ServiceException(
    httpStatusCode = HttpStatusCode.Conflict,
    errorCode = ServiceErrorCodes.AlreadyConnectedUsersCode,
    errorText = "already connected"
)