package com.silverbullet.core.di

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.silverbullet.core.databse.MongoDbFactory
import com.silverbullet.core.events.BWaveEventsEngine
import com.silverbullet.core.events.EventsEngine
import com.silverbullet.core.events.serialization.buildEventsSerializer
import com.silverbullet.core.security.hashing.HashingEngine
import com.silverbullet.core.security.hashing.HashingEngineImpl
import com.silverbullet.core.security.token.JwtTokenService
import com.silverbullet.core.security.token.TokenService
import com.silverbullet.core.utils.loadAccessTokenConfig
import com.silverbullet.core.utils.loadRefreshTokenConfig
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.slf4j.LoggerFactory

val coreModule = module {

    single {
        buildEventsSerializer()
    }

    single<EventsEngine> {
        BWaveEventsEngine(get())
    }

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

    single {
        // disable logs
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val rootLogger = loggerContext.getLogger("org.mongodb.driver")
        rootLogger.level = Level.INFO

        KMongo
            .createClient()
            .coroutine
            .getDatabase(MongoDbFactory.dbname)
    }
}