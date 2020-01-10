package ru.c57m7a.dbr.io.menu

import ru.c57m7a.dbr.Bot
import ru.c57m7a.dbr.ChatId
import ru.c57m7a.dbr.io.Filters.MessageFilters.userAndChatFilter
import ru.c57m7a.dbr.io.MenuRow
import com.sun.jdi.VirtualMachine
import ru.c57m7a.dbr.backend.jdi.connector
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.net.URL

suspend fun Bot.mainMenu(vm: VirtualMachine, chatId: ChatId) {
    val result = inlineMenu(
            SendMessage(chatId, "Select menu item:"),
        MenuRow(description(vm)),
        MenuRow(classes(vm)),
        MenuRow(threads(vm, chatId))
    )
    send(chatId, result)
}

suspend fun Bot.askForVm(userId: Int, chatId: ChatId): VirtualMachine {
    return ask(SendMessage(chatId, "Please run your application in debug mode and type URL for debugging")) { reply ->
        if (!userAndChatFilter(userId, chatId)(reply)) return@ask null

        val url = URL(reply.text)

        val arguments = connector.defaultArguments().apply {
            getValue("hostname").setValue(url.host)
            getValue("port").setValue(url.port.toString())
            getValue("timeout").setValue("3000")
        }

        connector.attach(arguments).also {
            logger.info("Connected to $url\n${it.description()}")
        }
    }
}