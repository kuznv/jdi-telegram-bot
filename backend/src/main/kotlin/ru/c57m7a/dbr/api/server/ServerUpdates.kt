package ru.c57m7a.dbr.api.server

import ru.c57m7a.dbr.api.UserId
import java.util.concurrent.atomic.AtomicInteger

object ServerUpdates {
    private fun getNextButtonId() = nextId.incrementAndGet()
    private fun getNextServerUpdateId() = nextId.incrementAndGet()

    fun Button(text: String) =
        ServerUpdate.Message.Button(getNextButtonId(), text)

    fun Message(userId: UserId, text: String, buttonRows: List<ButtonRow> = emptyList()) =
        ServerUpdate.Message(getNextServerUpdateId(), userId, text, buttonRows)

    private val nextId = AtomicInteger(0)
}