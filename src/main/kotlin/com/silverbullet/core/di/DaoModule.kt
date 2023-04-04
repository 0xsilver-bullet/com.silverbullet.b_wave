package com.silverbullet.core.di

import com.silverbullet.core.databse.dao.*
import org.koin.dsl.module

val daoModule = module {

    single<UserDao> {
        UserDaoImpl()
    }

    single<RefreshTokenDao> {
        RefreshTokenDaoImpl(get())
    }

    single<ConnectionDao> {
        ConnectionDaoImpl()
    }

    single<ChannelDao> {
        ChannelDaoImpl()
    }

    single<MessageDao> {
        MessageDaoImpl(get())
    }

}