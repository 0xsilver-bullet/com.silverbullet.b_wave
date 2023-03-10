package com.silverbullet.plugins

import com.silverbullet.di.appModule
import com.silverbullet.di.controllersModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin(){

    install(Koin){

        slf4jLogger()
        modules(
            appModule,
            controllersModule
        )
    }
}