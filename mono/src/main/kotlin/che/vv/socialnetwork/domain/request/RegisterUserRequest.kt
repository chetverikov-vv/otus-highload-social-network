package che.vv.socialnetwork.domain.request

import java.time.LocalDate

data class RegisterUserRequest(
    val firstName: String,
    val secondName: String,
    val birthdate: LocalDate,
    val biography: String,
    val city: String,
    val password: String
)