package che.vv.socialnetwork.dialogs.presentation

import che.vv.socialnetwork.dialogs.presentation.model.DialogMessage
import che.vv.socialnetwork.dialogs.presentation.model.Message
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dialog")
class DialogController {
    private val logger = KotlinLogging.logger{}

    @GetMapping("/{userId}/list")
    fun list(
        @PathVariable userId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String
    ): List<DialogMessage>{
        logger.info { "userId: $userId, header: $authHeader" }
        return listOf(DialogMessage(from = "dictumst", to = "ocurreret", text = "veniam"))
    }

    @PostMapping("/{userId}/send")
    fun send(
        @PathVariable userId: String,
        @RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String,
        @RequestBody message: Message
    ){
        logger.info { "userId: $userId, header: $authHeader, message: $message" }
    }
}