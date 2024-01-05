package che.vv.socialnetwork.domain.user

import che.vv.socialnetwork.domain.request.RegisterUserRequest
import java.time.LocalDate

data class User (
    val id: UserId,
    val firstName: String,
    val secondName: String,
    val birthdate: LocalDate,
    val biography: String,
    val city: String,
    val encryptedPassword: EncryptedPassword,
    val bearerToken: Token
) {
    companion object {
        fun createFrom(request: RegisterUserRequest): User? = with(request) {
            val encryptedPassword = EncryptedPassword.fromRawPassword(password) ?: return null
            User(
                id = UserId.generate(),
                firstName = firstName,
                secondName = secondName,
                birthdate = birthdate,
                biography = biography,
                city = city,
                encryptedPassword = encryptedPassword,
                bearerToken = Token.generate()
            )
        }
    }
}