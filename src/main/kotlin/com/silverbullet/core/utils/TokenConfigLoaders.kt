package com.silverbullet.core.utils

import com.silverbullet.core.security.token.model.JwtTokenConfig
import io.ktor.server.config.*

fun loadAccessTokenConfig(appConf: ApplicationConfig): JwtTokenConfig {
    val audience = appConf.property("jwt.audience").getString()
    val realm = appConf.property("jwt.realm").getString()
    val issuer = appConf.property("jwt.domain").getString()
    val secret = appConf.property("jwt.secret").getString()
    val expirationDate = appConf.property("jwt.access-token-expiration-millis").getString().toLong()

    return JwtTokenConfig(
        audience,
        realm,
        issuer,
        expirationDate,
        secret
    )
}

fun loadRefreshTokenConfig(appConf: ApplicationConfig): JwtTokenConfig {
    val audience = appConf.property("jwt.audience").getString()
    val realm = appConf.property("jwt.realm").getString()
    val issuer = appConf.property("jwt.domain").getString()
    val secret = appConf.property("jwt.secret").getString()
    val expirationDate = appConf.property("jwt.refresh-token-expiration-millis").getString().toLong()

    return JwtTokenConfig(
        audience,
        realm,
        issuer,
        expirationDate,
        secret
    )
}