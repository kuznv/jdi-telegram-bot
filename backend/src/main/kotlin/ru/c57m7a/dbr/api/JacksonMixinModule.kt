package ru.c57m7a.dbr.api

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.module.SimpleModule
import ru.c57m7a.dbr.api.client.ClientUpdate
import ru.c57m7a.dbr.api.server.ServerUpdate

object JacksonMixinModule : SimpleModule() {
    init {
        setMixInAnnotation(ClientUpdate::class.java, ClientUpdateMixin::class.java)
        setMixInAnnotation(ServerUpdate::class.java, ServerUpdateMixin::class.java)
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes(
        JsonSubTypes.Type(ClientUpdate.Message::class),
        JsonSubTypes.Type(ClientUpdate.ButtonClick::class)
    )
    abstract class ClientUpdateMixin

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes(
        JsonSubTypes.Type(ServerUpdate.Message::class)
    )
    abstract class ServerUpdateMixin
}