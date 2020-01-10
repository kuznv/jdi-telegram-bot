package ru.c57m7a.dbr

import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import ru.c57m7a.dbr.api.client.ClientUpdate
import ru.c57m7a.dbr.api.server.ServerUpdate
import java.net.URI

@Component
class Backend @Autowired constructor(private val restTemplate: RestTemplate) {
    private val baseUri = URI("http://localhost:8080/")
    private val updatesUri = baseUri.resolve("/update")

    suspend fun sendClientUpdate(clientUpdate: ClientUpdate): ServerUpdate =
        withContext(backendCoroutineContext) {
            restTemplate.postForObject<ServerUpdate>(updatesUri, clientUpdate)!!
        }
}