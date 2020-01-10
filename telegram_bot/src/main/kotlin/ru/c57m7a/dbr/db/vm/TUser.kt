package ru.c57m7a.dbr.db.vm

import org.telegram.telegrambots.meta.api.objects.User
import ru.c57m7a.dbr.api.UserId
import javax.persistence.*

@Entity
@Table(name = "usr")
class TUser(
    user: User,

    @JoinColumn(name = "chat_id")
    @ManyToOne(cascade = [CascadeType.MERGE])
    val chat: TChat
) {
    @Id
    @Column
    val id: UserId = user.id
}