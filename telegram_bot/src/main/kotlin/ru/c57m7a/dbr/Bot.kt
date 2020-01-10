package ru.c57m7a.dbr

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import ru.c57m7a.dbr.api.client.ClientUpdate
import ru.c57m7a.dbr.api.client.ClientUpdates
import ru.c57m7a.dbr.api.server.ServerUpdate
import ru.c57m7a.dbr.db.vm.TChat
import ru.c57m7a.dbr.db.vm.TUser
import ru.c57m7a.dbr.dto.UserRepository
import ru.c57m7a.dbr.utils.TelegramUpdate
import ru.c57m7a.dbr.utils.exec
import ru.c57m7a.dbr.utils.toKeyboard

@Component
class Bot @Autowired constructor(
    private val backend: Backend,
    private val userRepository: UserRepository,
    botOptions: DefaultBotOptions
) : TelegramLongPollingBot(botOptions) {

    val logger: Logger = LogManager.getLogger()

    override fun getBotUsername() = "jdebugger"

    override fun getBotToken() = TODO()

    override fun onUpdateReceived(update: TelegramUpdate) {
        GlobalScope.launch(telegramCoroutineContext) {
            val clientUpdate = update.process()
            if (clientUpdate == null) {
                logger.log(Level.ERROR, "Unhandled $update")
                return@launch
            }

            launch(serverCoroutineContext) {
                val serverUpdate = backend.sendClientUpdate(clientUpdate)
                serverUpdate.process()
            }
        }
    }

    private suspend fun TelegramUpdate.process(): ClientUpdate? {
        saveUserIfNew()
        return when {
            hasMessage() -> ClientUpdates.Message(message.from, message)
            hasEditedMessage() -> ClientUpdates.Message(message.from, editedMessage)
            hasCallbackQuery() -> ClientUpdates.ButtonClick(callbackQuery)
            else -> null
        }
    }

    private fun TelegramUpdate.saveUserIfNew() {
        val userId = message?.from?.id ?: return
        if (!userRepository.existsById(userId)) {
            val tChat = TChat(message.chat)
            val tUser = TUser(message.from, tChat)
            userRepository.save(tUser)
        }
    }

    private suspend fun ServerUpdate.process() {
        when (this) {
            is ServerUpdate.Message -> {
                val user = userRepository.findById(userId).get()
                val chat = user.chat
                val sendMessage = SendMessage(chat.id, text)

                if (buttonRows.isNotEmpty()) {
                    sendMessage.replyMarkup = ReplyKeyboardMarkup().apply { keyboard = buttonRows.toKeyboard() }
                }

                exec(sendMessage)
            }
        }
    }
}
