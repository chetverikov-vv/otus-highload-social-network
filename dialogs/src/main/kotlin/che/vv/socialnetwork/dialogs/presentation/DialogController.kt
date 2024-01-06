package che.vv.socialnetwork.dialogs.presentation

import che.vv.socialnetwork.dialogs.application.DialogService
import che.vv.socialnetwork.dialogs.domain.DialogElement
import che.vv.socialnetwork.dialogs.domain.UserId
import che.vv.socialnetwork.dialogs.domain.request.GetDialogRequest
import che.vv.socialnetwork.dialogs.domain.request.SendMessageRequest
import che.vv.socialnetwork.dialogs.presentation.model.BearerHeader
import che.vv.socialnetwork.dialogs.presentation.model.DialogMessage
import che.vv.socialnetwork.dialogs.presentation.model.Message
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/dialog")
class DialogController(
    private val dialogService: DialogService
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/{userId}/list")
    fun list(
        @PathVariable userId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestHeader("request-id") requestId: String?
    ): List<DialogMessage> = runCatching {
        val token = BearerHeader.fromHeaderString(authHeader)?.token ?: throw ResponseStatusException(BAD_REQUEST)
        val companionId = UserId.fromString(userId) ?: throw ResponseStatusException(BAD_REQUEST)
        val request = GetDialogRequest(token, companionId)
        when (val result = dialogService.getDialog(request)) {
            is DialogService.GetDialogResult.Success -> result.elements.toDto()
            DialogService.GetDialogResult.NOT_AUTH -> throw ResponseStatusException(UNAUTHORIZED)
            DialogService.GetDialogResult.INTERNAL_ERROR -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
        }
    }.fold(
        onSuccess = {
            logger.info {
                """
                    Time: ${LocalDateTime.now()}
                    RequestId: ${requestId ?: UUID.randomUUID().toString()}
                    Path: /dialog/$userId/list
                    AuthHeader: $authHeader
                    UserId: $userId
                    Result: $it
                    
                    """
            }
            it
        },
        onFailure = {
            logger.error(it) {
                """
                    Failure result for request:
                    Time: ${LocalDateTime.now()}
                    RequestId: ${requestId ?: UUID.randomUUID().toString()}
                    Path: /dialog/$userId/list
                    AuthHeader: $authHeader
                    UserId: $userId
                    Error message: ${it.message}
                    
                    """
            }
            throw it
        }
    )

    @PostMapping("/{userId}/send")
    fun send(
        @PathVariable userId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestHeader("request-id") requestId: String?,
        @RequestBody message: Message
    ) = runCatching {
        val token = BearerHeader.fromHeaderString(authHeader)?.token ?: throw ResponseStatusException(BAD_REQUEST)
        val companionId = UserId.fromString(userId) ?: throw ResponseStatusException(BAD_REQUEST)
        val request = SendMessageRequest(
            requesterToken = token,
            companionId = companionId,
            text = message.text,
            timestamp = Timestamp.valueOf(LocalDateTime.now())
        )
        when (dialogService.sendMessage(request)) {
            DialogService.SendMessageResult.Success -> Unit
            DialogService.SendMessageResult.NOT_AUTH -> throw ResponseStatusException(UNAUTHORIZED)
            DialogService.SendMessageResult.INTERNAL_ERROR -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
        }
    }.fold(
        onSuccess = {
            logger.info {
                """
                    Success send message request:
                    Time: ${LocalDateTime.now()}
                    RequestId: ${requestId ?: UUID.randomUUID().toString()}
                    Path: /dialog/$userId/send
                    AuthHeader: $authHeader
                    UserId: $userId
                    
                    """
            }
        },
        onFailure = {
            logger.error(it) {
                """
                    Failure result for request:
                    Time: ${LocalDateTime.now()}
                    RequestId: ${requestId ?: UUID.randomUUID().toString()}
                    Path: /dialog/$userId/send
                    AuthHeader: $authHeader
                    UserId: $userId
                    Error message: ${it.message}
                    
                    """
            }
            throw it
        }
    )

    private fun List<DialogElement>.toDto(): List<DialogMessage> = this.map {
        DialogMessage(
            from = it.senderId.value,
            to = it.receiverId.value,
            text = it.messageText
        )
    }
}
