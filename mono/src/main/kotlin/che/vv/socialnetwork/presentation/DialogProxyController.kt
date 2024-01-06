package che.vv.socialnetwork.presentation

import che.vv.socialnetwork.presentation.model.request.Message
import che.vv.socialnetwork.presentation.model.response.DialogMessage
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/dialog")
class DialogProxyController {
    private val logger = KotlinLogging.logger { }
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
        val requestId = generateRequestId()
        val url = """http://$dialogsHost:$dialogsPort/dialog/$userId/list"""
        val requestEntity = HttpEntity<String>(HttpHeaders().apply {
            set(HttpHeaders.AUTHORIZATION, authHeader)
            set("request-id", requestId)
        })
        runCatching {
            template.exchange(url, HttpMethod.GET, requestEntity, Array<DialogMessage>::class.java)
        }.fold(
            onSuccess = {
                logger.info {
                    """
                    Time: ${LocalDateTime.now()}
                    RequestId: $requestId
                    Path: /dialog/$userId/list
                    AuthHeader: $authHeader
                    Directed at: GET $url
                    Send result code: ${it.statusCode}
                    Send result data: ${it.body?.toList()}
                    
                    """
                }
                if (it.statusCode.is2xxSuccessful) return it.body!!.toList() else throw ResponseStatusException(it.statusCode)
            },
            onFailure = {
                logger.error(it) {
                    """
                    Failure to send request:
                    Time: ${LocalDateTime.now()}
                    RequestId: $requestId
                    Path: /dialog/$userId/list
                    AuthHeader: $authHeader
                    Directed at: GET $url
                    Error message: ${it.message}
                    
                    """
                }
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        )
    }

    @PostMapping("/{userId}/send")
    fun send(
        @PathVariable userId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody message: Message
    ) {
        val requestId = generateRequestId()
        val url = """http://$dialogsHost:$dialogsPort/dialog/$userId/send"""
        val requestEntity = HttpEntity<Message>(
            message,
            HttpHeaders().apply {
                set(HttpHeaders.AUTHORIZATION, authHeader)
                set("request-id", requestId)
            }
        )
        runCatching {
            template.exchange(url, HttpMethod.POST, requestEntity, Unit::class.java)
        }.fold(
            onSuccess = {
                logger.info {
                    """
                    Time: ${LocalDateTime.now()}
                    RequestId: $requestId
                    Path: /dialog/$userId/send
                    AuthHeader: $authHeader
                    Directed at: POST $url
                    Send result code: ${it.statusCode}
                    
                    """
                }
                if (it.statusCode.is2xxSuccessful) return
                else throw ResponseStatusException(it.statusCode)
            },
            onFailure = {
                logger.error(it) {
                    """
                    Failure to send request:
                    Time: ${LocalDateTime.now()}
                    RequestId: $requestId
                    Path: /dialog/$userId/send
                    AuthHeader: $authHeader
                    Directed at: GET $url
                    Error message: ${it.message}
                    
                    """
                }
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        )
    }

    private fun generateRequestId(): String = UUID.randomUUID().toString()
}