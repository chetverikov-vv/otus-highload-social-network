package che.vv.socialnetwork.dao

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class SqlTokenDao(
    private val dataSource: DataSource
) : TokenDao {

    private val template = NamedParameterJdbcTemplate(dataSource)
    private val userTableName = "users"
    private val findTokenQuery = """
        select 
            token
        from
            $userTableName
        where 
            id = :userId and password = :encryptedPassword
    """.trimIndent()

    override fun find(userId: String, encryptedPassword: String): Result<String?> = runCatching {
        template.query(
            findTokenQuery, mapOf(
                "userId" to userId,
                "encryptedPassword" to encryptedPassword
            )
        ) { rs, _ -> rs.getString("token") }.firstOrNull()
    }
}