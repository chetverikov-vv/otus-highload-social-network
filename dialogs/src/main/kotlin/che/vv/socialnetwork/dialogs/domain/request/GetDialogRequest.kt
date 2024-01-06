package che.vv.socialnetwork.dialogs.domain.request

import che.vv.socialnetwork.dialogs.domain.Token
import che.vv.socialnetwork.dialogs.domain.UserId

data class GetDialogRequest(
    val requesterToken: Token,
    val companionId: UserId
)
