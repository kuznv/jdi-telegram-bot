package ru.c57m7a.dbr.api.server

import ru.c57m7a.dbr.api.UserId

sealed class ServerUpdate(val id: ServerUpdateId, val userId: UserId) {

    class Message(
        serverUpdateId: ServerUpdateId,
        userId: UserId,
        val text: String,
        val buttonRows: List<ButtonRow> = emptyList()
    ) : ServerUpdate(serverUpdateId, userId) {

        class Button(val id: ButtonId, val text: String)
    }
}

typealias ButtonRow = List<ServerUpdate.Message.Button>
typealias ServerUpdateId = Int
typealias ButtonId = Int