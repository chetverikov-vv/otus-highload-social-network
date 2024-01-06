package che.vv.socialnetwork.dialogs.infrastructure

import che.vv.socialnetwork.dialogs.domain.Token
import che.vv.socialnetwork.dialogs.domain.UserId
import che.vv.socialnetwork.dialogs.domain.UserRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class PostgresUserRepository(private val dataSource: DataSource) : UserRepository {

    private val template = NamedParameterJdbcTemplate(dataSource)
    private val userTableName = "users"

    private val findIdByToken = """
        select
            id
        from 
            $userTableName
        where
            token = :token
    """.trimIndent()

    override fun findIdBy(token: Token): Result<UserId?> = runCatching {
        template.query(findIdByToken, mapOf("token" to token.value)) { rs, _ ->
            UserId.fromString(rs.getString("id"))!!
        }.firstOrNull()
    }
}