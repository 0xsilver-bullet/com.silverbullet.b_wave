package com.silverbullet.plugins

import com.silverbullet.core.di.coreModule
import com.silverbullet.core.di.daoModule
import com.silverbullet.di.appModule
import com.silverbullet.di.controllersModule
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {

    install(Koin) {

        slf4jLogger()
        modules(
            module { single { environment } },
            appModule,
            coreModule,
            daoModule,
            controllersModule
        )
    }
}