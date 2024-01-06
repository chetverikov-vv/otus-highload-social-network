package che.vv.socialnetwork.infrastructure

import che.vv.socialnetwork.domain.request.FindByPrefixRequest
import che.vv.socialnetwork.domain.user.*
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.sql.DataSource

@Repository
class PostgresUserRepository(private val dataSource: DataSource) : UserRepository {

    private val template = NamedParameterJdbcTemplate(dataSource)
    private val userTableName = "users"

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

    private val findByIdQuery = """
        select 
            id,
            first_name,
            second_name,
            birth_date,
            biography,
            city,
            password,
            token
        from
            $userTableName
        where
            id = :id
    """.trimIndent()

    private val findByPrefixesQuery = """
        select 
            id,
            first_name,
            second_name,
            birth_date,
            biography,
            city,
            password,
            token
        from
            $userTableName
        where
            first_name like :firstNamePrefix
            and second_name like :lastNamePrefix
        order by id
    """.trimIndent()

    private val findTokenByCredentialsQuery = """
        select 
            token
        from
            $userTableName
        where 
            id = :userId 
            and password = :password
    """.trimIndent()

    override fun save(user: User): Result<Unit> = runCatching {
        template.update(
            registerQuery, mapOf(
                "id" to user.id.value,
                "firstName" to user.firstName,
                "secondName" to user.secondName,
                "birthDate" to user.birthdate,
                "biography" to user.biography,
                "city" to user.city,
                "password" to user.encryptedPassword.value,
                "token" to user.bearerToken.value
            )
        )
    }

    override fun findById(id: UserId): Result<User?> = runCatching {
        template.query(
            findByIdQuery, mapOf("id" to id.value)
        ) { rs, _ ->
            User(
                id = UserId.fromString(rs.getString("first_name"))!!,
                firstName = rs.getString("first_name"),
                secondName = rs.getString("second_name"),
                birthdate = LocalDate.parse(rs.getString("birth_date")),
                biography = rs.getString("biography"),
                city = rs.getString("city"),
                encryptedPassword = EncryptedPassword.fromString(rs.getString("password"))!!,
                bearerToken = Token.fromString(rs.getString("token"))!!
            )
        }.firstOrNull()
    }

    override fun findBy(request: FindByPrefixRequest): Result<List<User>> = runCatching {
        template.query(
            findByPrefixesQuery, mapOf(
                "firstNamePrefix" to "${request.firstNamePrefix}%",
                "lastNamePrefix" to "${request.lastNamePrefix}%"
            )
        ) { rs, _ ->
            User(
                id = UserId.fromString(rs.getString("first_name"))!!,
                firstName = rs.getString("first_name"),
                secondName = rs.getString("second_name"),
                birthdate = LocalDate.parse(rs.getString("birth_date")),
                biography = rs.getString("biography"),
                city = rs.getString("city"),
                encryptedPassword = EncryptedPassword.fromString(rs.getString("password"))!!,
                bearerToken = Token.fromString(rs.getString("token"))!!
            )
        }
    }

    override fun findTokenBy(credentials: Credentials): Result<Token?> = runCatching {
        template.query(
            findTokenByCredentialsQuery, mapOf(
                "userId" to credentials.id.value,
                "password" to credentials.password.value
            )
        ) { rs, _ -> Token.fromString(rs.getString("token"))!! }.firstOrNull()
    }
}