package com.silverbullet.plugins

import com.silverbullet.core.databse.DatabaseFactory
import com.silverbullet.core.databse.MongoDbFactory
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking

fun Application.configureDatabases() {
    DatabaseFactory.init(environment.config)
    runBlocking {
        MongoDbFactory.init()
    }
}
