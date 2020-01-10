package ru.c57m7a.dbr.utils

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.updateshandlers.SentCallback
import ru.c57m7a.dbr.api.server.ButtonRow
import ru.c57m7a.dbr.api.server.ServerUpdate.Message.Button
import java.io.Serializable
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T : Serializable?> AbsSender.exec(method: BotApiMethod<T>): T =
    suspendCoroutine { cont ->
        executeAsync(method, object : SentCallback<T> {
            override fun onResult(method: BotApiMethod<T>?, response: T) {
                cont.resume(response)
            }

            override fun onException(method: BotApiMethod<T>?, exception: Exception) {
                cont.resumeWithException(exception)
            }

            override fun onError(method: BotApiMethod<T>?, apiException: TelegramApiRequestException) {
                cont.resumeWithException(apiException)
            }
        })
    }

typealias TelegramUpdate = Update

fun List<ButtonRow>.toKeyboard() =
    map(ButtonRow::toKeyboardRow)

fun ButtonRow.toKeyboardRow() =
    mapTo(
        KeyboardRow(),
        Button::toKeyboardButton
    )

fun Button.toKeyboardButton() =
    KeyboardButton(text)