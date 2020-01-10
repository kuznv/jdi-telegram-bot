package ru.c57m7a.dbr.io.menu

import com.sun.jdi.VirtualMachine
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.c57m7a.dbr.Bot
import ru.c57m7a.dbr.ChatId
import ru.c57m7a.dbr.io.MenuItem
import ru.c57m7a.dbr.io.MenuRow

internal fun Bot.threads(vm: VirtualMachine, chatId: ChatId) =
    MenuItem("Threads") {
        inlineMenu(
            SendMessage(chatId, "Threads:"),
            vm.allThreads().map { thread ->
                MenuRow(MenuItem(thread.name(), { thread.toString() }))
            }
        )
    }