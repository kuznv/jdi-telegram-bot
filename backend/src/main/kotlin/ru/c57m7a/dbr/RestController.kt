package ru.c57m7a.dbr

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.c57m7a.dbr.api.client.ClientUpdate
import ru.c57m7a.dbr.api.server.ServerUpdate
import ru.c57m7a.dbr.backend.Backend

@RestController
class RestController @Autowired constructor(private val backend: Backend) {

    @PostMapping("/update")
    fun update(@RequestBody clientUpdate: ClientUpdate): ServerUpdate =
        runBlocking {
            backend.processClientUpdate(clientUpdate)
        }
}