package che.vv.socialnetwork.dao

import che.vv.socialnetwork.service.model.RegistrationUser
import che.vv.socialnetwork.service.model.User
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.sql.DataSource

@Repository
class SqlUserDao(
    private val dataSource: DataSource
) : UserDao {

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
            first_name,
            second_name,
            birth_date,
            biography,
            city
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
            city
        from
            $userTableName
        where
            first_name like :firstNamePrefix
            and second_name like :lastNamePrefix
        order by id
    """.trimIndent()

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

    override fun findById(userId: String): Result<User?> = runCatching {
        template.query(
            findByIdQuery, mapOf(
                "id" to userId
            )
        ) { rs, _ ->
            User(
                id = userId,
                firstName = rs.getString("first_name"),
                secondName = rs.getString("second_name"),
                birthdate = LocalDate.parse(rs.getString("birth_date")),
                biography = rs.getString("biography"),
                city = rs.getString("city")
            )
        }.firstOrNull()
    }

    override fun findByPrefixes(firstNamePrefix: String, lastNamePrefix: String): Result<List<User>> = runCatching {
        template.query(
            findByPrefixesQuery, mapOf(
                "firstNamePrefix" to "$firstNamePrefix%",
                "lastNamePrefix" to "$lastNamePrefix%"
            )
        ) { rs, _ ->
            User(
                id = rs.getString("id"),
                firstName = rs.getString("first_name"),
                secondName = rs.getString("second_name"),
                birthdate = LocalDate.parse(rs.getString("birth_date")),
                biography = rs.getString("biography"),
                city = rs.getString("city")
            )
        }
    }
}