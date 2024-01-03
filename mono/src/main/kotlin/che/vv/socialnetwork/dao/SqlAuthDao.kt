package che.vv.socialnetwork.dao

import che.vv.socialnetwork.service.model.RegistrationUser
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class SqlAuthDao(
    private val dataSource: DataSource
) : AuthDao {

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
    private val registerQuery = """
        insert into
            $userTableName
            (
                id,
                first_name,
                second_name,
                birth_date,
                biography,
                city,
                password,
                token
            )
        values
            (
                :id,
                :firstName,
                :secondName,
                :birthDate,
                :biography,
                :city,
                :password,
                :token
            )
    """.trimIndent()

    override fun find(userId: String, encryptedPassword: String): Result<String?> = runCatching {
        template.query(
            findTokenQuery, mapOf(
                "userId" to userId,
                "encryptedPassword" to encryptedPassword
            )
        ) { rs, _ -> rs.getString("token") }.firstOrNull()
    }

    override fun register(user: RegistrationUser): Result<Unit> = runCatching {
        template.update(
            registerQuery, mapOf(
                "id" to user.id,
                "firstName" to user.firstName,
                "secondName" to user.secondName,
                "birthDate" to user.birthdate,
                "biography" to user.biography,
                "city" to user.city,
                "password" to user.encryptedPassword,
                "token" to user.token
            )
        )
    }
}