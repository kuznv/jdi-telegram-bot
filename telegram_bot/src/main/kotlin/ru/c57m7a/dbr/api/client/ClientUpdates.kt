package ru.c57m7a.dbr.api.client

import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import ru.c57m7a.dbr.api.UserId
import java.util.concurrent.atomic.AtomicInteger

object ClientUpdates {
    private fun getNextClientUpdateId() = nextId.incrementAndGet()

    fun ButtonClick(callbackQuery: CallbackQuery) =
        ClientUpdate.ButtonClick(
            getNextClientUpdateId(),
            callbackQuery.from.id,
            callbackQuery.message.text.toInt()
        )

    fun Message(user: User, message: Message) =
        ClientUpdate.Message(getNextClientUpdateId(), user.id, message.text)

    private val nextId = AtomicInteger(0)
}