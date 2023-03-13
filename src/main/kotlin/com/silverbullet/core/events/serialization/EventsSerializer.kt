package com.silverbullet.core.events.serialization

import com.silverbullet.core.events.client.ClientEvent
import com.silverbullet.core.events.client.SendDmMessageEvent
import com.silverbullet.core.events.server.ConnectedToUserEvent
import com.silverbullet.core.events.server.FriendOnlineStatusEvent
import com.silverbullet.core.events.server.ReceivedDmMessageEvent
import com.silverbullet.core.events.server.ServerEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val eventsSerializationModule = SerializersModule {

    polymorphic(ServerEvent::class) {

        subclass(ConnectedToUserEvent::class)

        subclass(ReceivedDmMessageEvent::class)

        subclass(FriendOnlineStatusEvent::class)

    }

    polymorphic(ClientEvent::class) {

        subclass(SendDmMessageEvent::class)

    }
}

fun buildEventsSerializer(): Json {

    return Json {
        classDiscriminator = "event"
        serializersModule = eventsSerializationModule
    }
}