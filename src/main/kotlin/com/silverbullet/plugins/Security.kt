package com.silverbullet.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.silverbullet.core.security.token.model.JwtTokenConfig
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val jwtTokenConfig by inject<JwtTokenConfig>(qualifier = named("accessTokenConfig"))

    authentication {

        jwt {
            val jwtAudience = jwtTokenConfig.audience
            realm = jwtTokenConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtTokenConfig.secret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtTokenConfig.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience))
                    JWTPrincipal(credential.payload)
                else
                    null
            }
        }

    }
}
