package ru.c57m7a.dbr.db.vm

import org.telegram.telegrambots.meta.api.objects.Chat
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
class TChat(chat: Chat) {
    @Id
    @Column
    val id: ChatId = chat.id
}

typealias ChatId = Long