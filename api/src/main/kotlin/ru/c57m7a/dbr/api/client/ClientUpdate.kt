package ru.c57m7a.dbr.api.client

import ru.c57m7a.dbr.api.UserId

sealed class ClientUpdate(val id: ClientUpdateId, val userId: UserId) {

    class Message(
        id: ClientUpdateId,
        userId: UserId,
        val text: String
    ) : ClientUpdate(id, userId)

    class ButtonClick(
        id: ClientUpdateId,
        userId: UserId,
        val buttonId: ButtonId
    ) : ClientUpdate(id, userId)
}

typealias ClientUpdateId = Int
typealias ButtonId = Int