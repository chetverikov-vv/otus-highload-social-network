package che.vv.socialnetwork.service.model

import java.time.LocalDate

data class RegistrationUser(
    val id: String,
    val firstName: String,
    val secondName: String,
    val birthdate: LocalDate,
    val biography: String,
    val city: String,
    val encryptedPassword: String,
    val token: String
)
