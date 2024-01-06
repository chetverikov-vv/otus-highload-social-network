package che.vv.socialnetwork.dialogs.application

import che.vv.socialnetwork.dialogs.domain.DialogElement
import che.vv.socialnetwork.dialogs.domain.request.GetDialogRequest
import che.vv.socialnetwork.dialogs.domain.request.SendMessageRequest

interface DialogService {
    fun getDialog(request: GetDialogRequest): GetDialogResult
    fun sendMessage(request: SendMessageRequest): SendMessageResult

    sealed class GetDialogResult {
        data class Success(val elements: List<DialogElement>) : GetDialogResult()
        object NOT_AUTH : GetDialogResult()
        object INTERNAL_ERROR : GetDialogResult()
    }

    sealed class SendMessageResult {
        object Success : SendMessageResult()
        object NOT_AUTH : SendMessageResult()
        object INTERNAL_ERROR : SendMessageResult()
    }
}