package che.vv.socialnetwork.controller

import che.vv.socialnetwork.controller.model.request.LoginRequest
import che.vv.socialnetwork.controller.model.response.LoginResponse
import che.vv.socialnetwork.service.AuthService
import che.vv.socialnetwork.service.AuthService.LoginResult.*
import mu.KotlinLogging
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse =
        when (val result = authService.login(loginRequest)) {
            is Success -> LoginResponse(result.token)
            NotFound -> throw ResponseStatusException(NOT_FOUND)
            InvalidData -> throw ResponseStatusException(BAD_REQUEST)
            InternalFailure -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
        }
}