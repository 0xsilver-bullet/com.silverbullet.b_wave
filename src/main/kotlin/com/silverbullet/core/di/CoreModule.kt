package com.silverbullet.core.di

import com.silverbullet.core.security.HashingEngine
import com.silverbullet.core.security.HashingEngineImpl
import org.koin.dsl.module

val coreModule = module {

    single<HashingEngine> {
        HashingEngineImpl()
    }
}