package com.silverbullet.core.di

import com.silverbullet.core.security.hashing.HashingEngine
import com.silverbullet.core.security.hashing.HashingEngineImpl
import com.silverbullet.core.security.token.JwtTokenService
import com.silverbullet.core.security.token.TokenService
import com.silverbullet.core.utils.loadAccessTokenConfig
import com.silverbullet.core.utils.loadRefreshTokenConfig
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreModule = module {

    single<HashingEngine> {
        HashingEngineImpl()
    }

    single(named("accessTokenConfig")) {
        val appEnv = get<ApplicationEnvironment>()
        loadAccessTokenConfig(appEnv.config)
    }

    single(named("refreshTokenConfig")) {
        val appEnv = get<ApplicationEnvironment>()
        loadRefreshTokenConfig(appEnv.config)
    }

    single<TokenService> {
        JwtTokenService(
            get(qualifier = named("accessTokenConfig")),
            get(qualifier = named("refreshTokenConfig"))
        )
    }
}