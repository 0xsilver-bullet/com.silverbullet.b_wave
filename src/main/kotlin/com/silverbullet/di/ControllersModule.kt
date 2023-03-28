package com.silverbullet.di

import com.silverbullet.feature_auth.AuthController
import com.silverbullet.feature_connection.ConnectionsController
import com.silverbullet.feature_dm.DmController
import com.silverbullet.feature_profile.ProfileController
import org.koin.dsl.module

val controllersModule = module {

    single {
        AuthController(get(), get(), get(), get())
    }

    single {
        ConnectionsController(get(), get(), get())
    }

    single {
        DmController(get())
    }

    single {
        ProfileController(get())
    }
}