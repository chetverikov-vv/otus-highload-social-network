package che.vv.socialnetwork.presentation.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class RegisterRequest(
    @get:JsonProperty("first_name") val firstName: String,
    @get:JsonProperty("second_name") val secondName: String,
    @get:JsonProperty("birthdate") val birthDate: String,
    val biography: String,
    val city: String,
    val password: String
)
