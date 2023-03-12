package com.silverbullet.di

import com.silverbullet.feature_auth.AuthController
import com.silverbullet.feature_connection.ConnectionsController
import org.koin.dsl.module

val controllersModule = module {

    single {
        AuthController(get(), get(), get(), get())
    }

    single {
        ConnectionsController(get(), get())
    }
}