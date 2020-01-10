package ru.c57m7a.dbr.db

import kotlinx.coroutines.channels.Channel
import ru.c57m7a.dbr.api.client.ClientUpdate
import ru.c57m7a.dbr.api.server.ServerUpdate
import javax.persistence.*
import kotlin.jvm.Transient

typealias ClientToServerUpdate = (ClientUpdate) -> ServerUpdate

@Entity
@Table(name = "usr")
class User(
    @Id val id: Int,

    @OneToMany
    @JoinColumn
    val profiles: List<VMProfile> = listOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User
        return id == other.id
    }

    override fun hashCode() = id
}