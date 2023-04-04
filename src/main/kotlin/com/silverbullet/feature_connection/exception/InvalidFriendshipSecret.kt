package com.silverbullet.feature_connection.exception

import com.silverbullet.core.utils.exceptions.ServiceErrorCodes
import com.silverbullet.core.utils.exceptions.ServiceException
import io.ktor.http.*

class InvalidFriendshipSecret: ServiceException(
    httpStatusCode = HttpStatusCode.BadRequest,
    errorCode = ServiceErrorCodes.InvalidFriendshipSecretCode,
    errorText = "the secret you sent is invalid"
)