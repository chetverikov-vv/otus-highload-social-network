package che.vv.socialnetwork.presentation

import che.vv.socialnetwork.presentation.model.request.Message
import che.vv.socialnetwork.presentation.model.response.DialogMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/dialog")
class DialogProxyController {
    private val template = RestTemplate()

    @Value("\${dialogs.host}")
    private lateinit var dialogsHost: String

    @Value("\${dialogs.port}")
    private var dialogsPort: Int = 0

    @GetMapping("/{userId}/list")
    fun list(
        @PathVariable userId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String
    ): List<DialogMessage> {
        val url = """http://$dialogsHost:$dialogsPort/dialog/$userId/list"""
        val requestEntity = HttpEntity<String>(HttpHeaders().apply { set(HttpHeaders.AUTHORIZATION, authHeader) })
        val result = template.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            Array<DialogMessage>::class.java
        )
        if (result.statusCode.is2xxSuccessful) return result.body!!.toList() else throw ResponseStatusException(result.statusCode)
    }

    @PostMapping("/{userId}/send")
    fun send(
        @PathVariable userId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody message: Message
    ):Unit{
        val url = """http://$dialogsHost:$dialogsPort/dialog/$userId/send"""
        val requestEntity = HttpEntity<Message>(
            message,
            HttpHeaders().apply { set(HttpHeaders.AUTHORIZATION, authHeader) }
        )
        val result = template.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            Unit::class.java
        )
        if (result.statusCode.is2xxSuccessful) return else throw ResponseStatusException(result.statusCode)
    }
}