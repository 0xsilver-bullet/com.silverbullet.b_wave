package com.silverbullet.core.events.serialization

import com.silverbullet.core.events.client.ClientEvent
import com.silverbullet.core.events.client.SeenMessagesEvent
import com.silverbullet.core.events.client.SendMessageEvent
import com.silverbullet.core.events.server.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val eventsSerializationModule = SerializersModule {

    polymorphic(ServerEvent::class) {

        subclass(ConnectedToUserEvent::class)

        subclass(ReceivedMessageEvent::class)

        subclass(FriendOnlineStatusEvent::class)

        subclass(UpdateMessagesEvent::class)

        subclass(MessageSentEvent::class)

        subclass(AddedToChannel::class)

    }

    polymorphic(ClientEvent::class) {

        subclass(SendMessageEvent::class)

        subclass(SeenMessagesEvent::class)

    }
}

fun buildEventsSerializer(): Json {

    return Json {
        classDiscriminator = "event"
        serializersModule = eventsSerializationModule
    }
}