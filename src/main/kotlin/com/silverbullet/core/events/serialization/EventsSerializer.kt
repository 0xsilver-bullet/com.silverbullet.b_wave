package com.silverbullet.core.events.serialization

import com.silverbullet.core.events.client.ClientEvent
import com.silverbullet.core.events.client.SeenDmMessageEvent
import com.silverbullet.core.events.client.SendDmMessageEvent
import com.silverbullet.core.events.server.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val eventsSerializationModule = SerializersModule {

    polymorphic(ServerEvent::class) {

        subclass(ConnectedToUserEvent::class)

        subclass(ReceivedDmMessageEvent::class)

        subclass(FriendOnlineStatusEvent::class)

        subclass(UpdateDmMessageEvent::class)

        subclass(DmSentEvent::class)

    }

    polymorphic(ClientEvent::class) {

        subclass(SendDmMessageEvent::class)

        subclass(SeenDmMessageEvent::class)

    }
}

fun buildEventsSerializer(): Json {

    return Json {
        classDiscriminator = "event"
        serializersModule = eventsSerializationModule
    }
}