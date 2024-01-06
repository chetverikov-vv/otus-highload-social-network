package che.vv.socialnetwork.dialogs.application

import che.vv.socialnetwork.dialogs.domain.DialogElement
import che.vv.socialnetwork.dialogs.domain.DialogElementRepository
import che.vv.socialnetwork.dialogs.domain.UserRepository
import che.vv.socialnetwork.dialogs.domain.request.GetDialogRequest
import che.vv.socialnetwork.dialogs.domain.request.SendMessageRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class DialogServiceImpl(
    private val dialogElementRepository: DialogElementRepository,
    private val userRepository: UserRepository
) : DialogService {
    private val logger = KotlinLogging.logger { }
    override fun getDialog(request: GetDialogRequest): DialogService.GetDialogResult {
        val requesterId = userRepository.findIdBy(request.requesterToken).getOrElse {
            logger.error(it) { "Failure to find userId by token ${request.requesterToken}" }
            return DialogService.GetDialogResult.INTERNAL_ERROR
        } ?: return DialogService.GetDialogResult.NOT_AUTH
        return dialogElementRepository.findBy(requesterId, request.companionId).fold(
            onSuccess = { DialogService.GetDialogResult.Success(it) },
            onFailure = {
                logger.error(it) { "Failure to find dialog elements by requesterId $requesterId and companionId ${request.companionId}" }
                DialogService.GetDialogResult.INTERNAL_ERROR
            }
        )
    }

    override fun sendMessage(request: SendMessageRequest): DialogService.SendMessageResult {
        val senderId = userRepository.findIdBy(request.requesterToken).getOrElse {
            logger.error(it) { "Failure to find userId by token ${request.requesterToken}" }
            return DialogService.SendMessageResult.INTERNAL_ERROR
        } ?: return DialogService.SendMessageResult.NOT_AUTH
        val element = DialogElement.new(
            senderId = senderId,
            receiverId = request.companionId,
            messageText = request.text,
            timestamp = request.timestamp

        )
        return dialogElementRepository.save(element).fold(
            onSuccess = { DialogService.SendMessageResult.Success },
            onFailure = {
                logger.error(it) { "Failure to save element $element" }
                DialogService.SendMessageResult.INTERNAL_ERROR
            }
        )
    }
}