package che.vv.socialnetwork.dialogs.infrastructure

import che.vv.socialnetwork.dialogs.domain.DialogElement
import che.vv.socialnetwork.dialogs.domain.DialogElementRepository
import che.vv.socialnetwork.dialogs.domain.UserId
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class PostgresDialogElementRepository(private val dataSource: DataSource) : DialogElementRepository {

    private val template = NamedParameterJdbcTemplate(dataSource)
    private val dialogsTableName = "dialogs"

    private val saveElementQuery = """
        insert
            into $dialogsTableName
            (
                id,
                sender,
                receiver,
                text,
                send_timestamp
            )
        values
            (
                :id,
                :sender,
                :receiver,
                :text,
                :timestamp
            )
    """.trimIndent()

    private val findDialogQuery = """
        select
            id,
            sender,
            receiver,
            text,
            send_timestamp
        from
            $dialogsTableName
        where
            sender = :firstUserId and receiver = :secondUserId
            or sender = :secondUserId and receiver = :firstUserId
        order by send_timestamp 
    """.trimIndent()

    override fun save(element: DialogElement): Result<Unit> = runCatching {
        template.update(
            saveElementQuery,
            mapOf(
                "id" to element.id,
                "sender" to element.senderId.value,
                "receiver" to element.receiverId.value,
                "text" to element.messageText,
                "timestamp" to element.timestamp
            )
        )
    }

    override fun findBy(firstUserId: UserId, secondUserId: UserId): Result<List<DialogElement>> = runCatching {
        template.query(
            findDialogQuery,
            mapOf(
                "firstUserId" to firstUserId.value,
                "secondUserId" to secondUserId.value
            )
        ) { rs, _ ->
            DialogElement.restore(
                id = rs.getString("id"),
                senderId = UserId.fromString(rs.getString("sender"))!!,
                receiverId = UserId.fromString(rs.getString("receiver"))!!,
                messageText = rs.getString("text"),
                timestamp = rs.getTimestamp("send_timestamp")
            )
        }
    }
}