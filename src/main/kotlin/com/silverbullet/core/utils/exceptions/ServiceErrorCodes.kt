package com.silverbullet.core.utils.exceptions


object ServiceErrorCodes {

    const val UnexpectedServiceErrorCode = 0

    const val InvalidRequestBodyCode = 1

    // Authentication Codes
    const val UsernameAlreadyExistsCode = 2

    const val UserNotFoundCode = 3

    const val InvalidCredentialsCode = 4

    const val InvalidRefreshTokenCode = 5

    // Connection Codes
    const val AlreadyConnectedUsersCode = 6
}