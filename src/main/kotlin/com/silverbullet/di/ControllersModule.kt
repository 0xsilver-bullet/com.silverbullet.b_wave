package com.silverbullet.di

import com.silverbullet.feature_auth.AuthController
import com.silverbullet.feature_channels.ChannelsController
import com.silverbullet.feature_connection.ConnectionsController
import com.silverbullet.feature_messages.MessageController
import com.silverbullet.feature_profile.ProfileController
import org.koin.dsl.module

val controllersModule = module {

    single {
        AuthController(get(), get(), get(), get())
    }

    single {
        ConnectionsController(get(), get(), get(), get(), get())
    }

    single {
        MessageController(get())
    }

    single {
        ProfileController(get())
    }

    single {
        ChannelsController(get())
    }
}