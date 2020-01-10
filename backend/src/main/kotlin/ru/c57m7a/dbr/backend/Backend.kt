package ru.c57m7a.dbr.backend

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.c57m7a.dbr.api.UserId
import ru.c57m7a.dbr.api.client.ClientUpdate
import ru.c57m7a.dbr.api.server.ServerUpdate
import ru.c57m7a.dbr.api.server.ServerUpdates
import ru.c57m7a.dbr.db.ClientToServerUpdate
import ru.c57m7a.dbr.db.User
import ru.c57m7a.dbr.db.VMProfile
import ru.c57m7a.dbr.dto.UserRepository
import java.util.concurrent.ConcurrentHashMap

@Component
class Backend @Autowired constructor(private val userRepository: UserRepository) {
    private val userUpdates = ConcurrentHashMap<User, Channel<ClientToServerUpdate>>()

    private val User.updates: Channel<ClientToServerUpdate>
        get() = userUpdates.computeIfAbsent(this) {
            println("created for $id")
            Channel()
        }

    suspend fun processClientUpdate(clientUpdate: ClientUpdate): ServerUpdate {
        var user = getUser(clientUpdate)
        if (user == null) {
            user = registerNewUser(clientUpdate.userId)
            GlobalScope.launch { handleUser(user) }
        }
        val action = user.updates.receive()
        return action(clientUpdate)
    }

    private fun getUser(clientUpdate: ClientUpdate): User? {
        val userId = clientUpdate.userId

        val userOptional = userRepository.findById(userId)
        return if (userOptional.isPresent) userOptional.get() else null
    }

    private suspend fun handleUser(user: User) {
        while (true) {
            user.updates.send { clientUpdate ->
                ServerUpdates.Message(
                    user.id, "Test:", listOf(
                        listOf(
                            ServerUpdates.Button("1"),
                            ServerUpdates.Button("2")
                        ),
                        listOf(
                            ServerUpdates.Button("3")
                        )
                    )
                )
            }
            user.updates.send { clientUpdate ->
                ServerUpdates.Message(
                    user.id, "Got ${(clientUpdate as? ClientUpdate.Message)?.text}", listOf(
                        listOf(
                            ServerUpdates.Button("3"),
                            ServerUpdates.Button("4")
                        ),
                        listOf(
                            ServerUpdates.Button("5")
                        )
                    )
                )
            }
        }
    }

    private fun selectProfile(user: User): VMProfile {
        TODO()
    }


    private fun registerNewUser(userId: UserId): User {
        val newUser = User(userId)
        return userRepository.save(newUser)
    }
}