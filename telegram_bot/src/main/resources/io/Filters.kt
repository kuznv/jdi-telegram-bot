package ru.c57m7a.dbr.io

import ru.c57m7a.dbr.ChatId
import org.telegram.telegrambots.api.objects.CallbackQuery
import org.telegram.telegrambots.api.objects.Message

typealias Filter<T> = (T) -> Boolean

object Filters {
    val all: Filter<Any?> = { true }

    object MessageFilters {
        fun chatFilter(chatId: ChatId): Filter<Message> =
                { it.chatId.toString() == chatId }

        fun userAndChatFilter(userId: Int, chatId: ChatId): Filter<Message> =
                { it.chatId.toString() == chatId && it.from.id == userId }
    }

    object CallbackQueryFilters {
        fun userFilter(userId: Int): Filter<CallbackQuery> =
                { it.from.id == userId }
    }
}