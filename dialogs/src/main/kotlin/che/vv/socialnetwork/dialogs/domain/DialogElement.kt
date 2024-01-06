package che.vv.socialnetwork.dialogs.domain

import java.sql.Timestamp
import java.util.*

data class DialogElement private constructor(
    val id: String,
    val senderId: UserId,
    val receiverId: UserId,
    val messageText: String,
    val timestamp: Timestamp
) {
    companion object {
        fun new(
            senderId: UserId,
            receiverId: UserId,
            messageText: String,
            timestamp: Timestamp
        ): DialogElement = DialogElement(
            id = UUID.randomUUID().toString(),
            senderId = senderId,
            receiverId = receiverId,
            messageText = messageText,
            timestamp = timestamp
        )

        fun restore(
            id: String,
            senderId: UserId,
            receiverId: UserId,
            messageText: String,
            timestamp: Timestamp
        ) = DialogElement(
            id = id,
            senderId = senderId,
            receiverId = receiverId,
            messageText = messageText,
            timestamp = timestamp
        )
    }
}
