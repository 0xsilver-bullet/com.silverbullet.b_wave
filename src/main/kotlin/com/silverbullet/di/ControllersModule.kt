package com.silverbullet.di

import com.silverbullet.feature_auth.AuthController
import org.koin.dsl.module

val controllersModule = module {

    single {
        AuthController(get(), get())
    }
}