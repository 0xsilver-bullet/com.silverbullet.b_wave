package com.silverbullet.utils

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

val ApplicationCall.userId: Int?
    get() = principal<JWTPrincipal>()?.get("userId")?.toIntOrNull()