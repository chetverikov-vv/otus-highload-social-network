package che.vv.socialnetwork.service.model

import java.time.LocalDate

data class User(
    val id: String,
    val firstName: String,
    val secondName: String,
    val birthdate: LocalDate,
    val biography: String,
    val city: String
)