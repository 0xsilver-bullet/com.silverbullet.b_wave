package com.silverbullet.plugins

import com.silverbullet.core.databse.DatabaseFactory
import io.ktor.server.application.*

fun Application.configureDatabases() {
    DatabaseFactory.init(environment.config)
}
