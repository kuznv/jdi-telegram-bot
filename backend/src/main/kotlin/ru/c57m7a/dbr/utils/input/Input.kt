package ru.c57m7a.dbr.utils.input

import ru.c57m7a.dbr.backend.Backend
import ru.c57m7a.dbr.db.User

suspend fun Backend.ask(user: User, message: String) {
    val result: String
//    user.updates.send { reply ->
//        result = reply as? ClientUpdate.Message
//        ServerUpdates.Message(user.id, (reply as? ClientUpdate.Message)?.text ?: message)
//    }
}