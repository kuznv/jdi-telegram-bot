package ru.c57m7a.dbr.io

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

class MenuItem<out T>(val message: String, val onSelect: suspend (Message) -> T?)
class MenuRow<out T>(vararg val items: MenuItem<T>)

suspend fun <T> AbstractTelegramBot.menu(sendMessage: SendMessage, menuRows: List<MenuRow<T>>): T {
    sendMessage.replyMarkup = ReplyKeyboardMarkup().apply {
        keyboard = menuRows.map { row ->
            row.items.mapTo(KeyboardRow()) { menuItem ->
                KeyboardButton(menuItem.message)
            }
        }
    }
    val menuItems = menuRows.flatMap { it.items.toList() }

    return ask(sendMessage) { replyMessage ->
        menuItems
            .find { it.message.equals(replyMessage.text, ignoreCase = true) }
            ?.let { it.onSelect(replyMessage) }
    }
}

suspend fun <T> AbstractTelegramBot.inlineMenu(sendMessage: SendMessage, vararg menuRows: MenuRow<T>): T =
    inlineMenu(sendMessage, menuRows.toList())

suspend fun <T> AbstractTelegramBot.inlineMenu(sendMessage: SendMessage, menuRows: List<MenuRow<T>>): T {
    val callbackId = ThreadLocalRandom.current().nextLong()
    sendMessage.replyMarkup = InlineKeyboardMarkup().apply {
        keyboard = menuRows.mapIndexed { rowIndex, row ->
            row.items.mapIndexed { columnIndex, menuItem ->
                InlineKeyboardButton(menuItem.message).apply {
                    callbackData = "$callbackId;$rowIndex;$columnIndex"
                }
            }
        }
    }
    val message = exec(sendMessage)

    return updates.openSubscription().use { updates ->
        var result: T? = null
        do {
            updates.receive().use useUpdate@{ update: Update ->
                if (!update.hasCallbackQuery() || update.callbackQuery.data.split(';')[0].toLong() != callbackId)
                    return@useUpdate false
                val callbackQuery = update.callbackQuery
                val (row, column) = callbackQuery.data.split(';').drop(1).map(String::toInt)
                val menuItem = menuRows[row].items[column]

                result = menuItem.onSelect(callbackQuery.message)
                true
            }
        } while (result == null)

        exec(DeleteMessage(sendMessage.chatId, message.messageId))
        result!!
    }
}

suspend fun <T> User.ask(message: Message, parse: suspend (reply: Message) -> T?): T {
    messages
    return updates.openSubscription().use { updates ->
        var result: T? = null
        do {
            exec(sendMessage)

            updates.receive().use useUpdate@{ update ->
                try {
                    result = parse(update.anyMessage ?: return@useUpdate false)
                    result == null
                } catch (e: Exception) {
                    sendException(sendMessage.chatId, e)
                    false
                }
            }
        } while (result == null)
        result!!
    }
}