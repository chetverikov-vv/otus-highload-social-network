package che.vv.socialnetwork.presentation

import che.vv.socialnetwork.application.AuthService
import che.vv.socialnetwork.application.AuthService.LoginResult.*
import che.vv.socialnetwork.domain.user.Credentials
import che.vv.socialnetwork.domain.user.EncryptedPassword
import che.vv.socialnetwork.domain.user.UserId
import che.vv.socialnetwork.presentation.model.request.LoginRequest
import che.vv.socialnetwork.presentation.model.response.LoginResponse
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        val credentials = loginRequest.toCredentials() ?: throw ResponseStatusException(BAD_REQUEST)
        return when (val result = authService.login(credentials)) {
            is Success -> LoginResponse(result.token.value)
            NotFound -> throw ResponseStatusException(NOT_FOUND)
            InternalFailure -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
        }
    }

    private fun LoginRequest.toCredentials(): Credentials? {
        val userId = UserId.fromString(this.id) ?: return null
        val encryptedPassword = EncryptedPassword.fromRawPassword(this.password) ?: return null
        return Credentials(userId, encryptedPassword)
    }
}