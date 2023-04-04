package com.silverbullet.feature_connection.exception

import com.silverbullet.core.utils.exceptions.ServiceErrorCodes
import com.silverbullet.core.utils.exceptions.ServiceException
import io.ktor.http.*

class FriendshipSecretExpired : ServiceException(
    httpStatusCode = HttpStatusCode.Conflict,
    errorCode = ServiceErrorCodes.FriendshipSecretExpiredCode,
    errorText = "this secret is expired"
)