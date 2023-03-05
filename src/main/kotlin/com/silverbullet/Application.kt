package com.silverbullet

import io.ktor.server.application.*
import com.silverbullet.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureDatabases()
    configureMonitoring()
    configureHTTP()
    configureSockets()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureStatusPages()
}
