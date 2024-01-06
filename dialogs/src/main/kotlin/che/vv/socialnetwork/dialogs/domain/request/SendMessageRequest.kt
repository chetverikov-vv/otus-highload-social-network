package che.vv.socialnetwork.dialogs.domain.request

import che.vv.socialnetwork.dialogs.domain.Token
import che.vv.socialnetwork.dialogs.domain.UserId
import java.sql.Timestamp

data class SendMessageRequest(
    val requesterToken: Token,
    val companionId: UserId,
    val text: String,
    val timestamp: Timestamp
)