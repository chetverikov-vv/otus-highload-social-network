package che.vv.socialnetwork.presentation.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UserResponse(
    val id: String,
    @get:JsonProperty("first_name") val firstName: String,
    @get:JsonProperty("second_name") val secondName: String,
    @get:JsonProperty("birthdate") val birthDate: String,
    val biography: String,
    val city: String
)
