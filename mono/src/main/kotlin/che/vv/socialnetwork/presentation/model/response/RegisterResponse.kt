package che.vv.socialnetwork.presentation.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class RegisterResponse(
    @get:JsonProperty("user_id") val userId: String
)
