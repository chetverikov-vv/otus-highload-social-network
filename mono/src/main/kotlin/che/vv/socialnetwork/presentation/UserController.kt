package che.vv.socialnetwork.presentation

import che.vv.socialnetwork.domain.user.User
import che.vv.socialnetwork.domain.user.UserId
import che.vv.socialnetwork.domain.request.FindByPrefixRequest
import che.vv.socialnetwork.domain.request.RegisterUserRequest
import che.vv.socialnetwork.presentation.model.request.RegisterRequest
import che.vv.socialnetwork.presentation.model.response.RegisterResponse
import che.vv.socialnetwork.presentation.model.response.UserResponse
import che.vv.socialnetwork.application.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@RestController()
@RequestMapping("user")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): RegisterResponse {
        val transformedRequest = registerRequest.toDomainRequest() ?: throw ResponseStatusException(BAD_REQUEST)
        return when (val result = userService.register(transformedRequest)) {
            is UserService.RegisterResult.Success -> RegisterResponse(result.userId.value)
            UserService.RegisterResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
            UserService.RegisterResult.InvalidData -> throw ResponseStatusException(BAD_REQUEST)
        }
    }


    @GetMapping("/get/{id}")
    fun getById(@PathVariable id: String): UserResponse{
        val userId = UserId.fromString(id) ?: throw ResponseStatusException(BAD_REQUEST)
        return when (val result = userService.findById(userId)) {
            is UserService.FindResult.Success -> result.user.toResponse()
            UserService.FindResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
            UserService.FindResult.NotFound -> throw ResponseStatusException(HttpStatus.NOT_FOUND)
            UserService.FindResult.InvalidData -> throw ResponseStatusException(BAD_REQUEST)
        }
    }

    @GetMapping("/search")
    fun searchByFirstNamePrefixAndLastNamePrefix(
        @RequestParam("first_name") firstNamePrefix: String,
        @RequestParam("last_name") lastNamePrefix: String
    ): List<UserResponse> {
        val request = FindByPrefixRequest.from(firstNamePrefix,lastNamePrefix) ?: throw ResponseStatusException(BAD_REQUEST)
        return when (val result = userService.findByPrefixes(request)) {
            UserService.FindPrefixResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
            is UserService.FindPrefixResult.Success -> result.users.map { it.toResponse() }
        }
    }

    private fun User.toResponse() = UserResponse(
        id = this.id.value,
        firstName = this.firstName,
        secondName = this.secondName,
        birthDate = this.birthdate.toString(),
        biography = this.biography,
        city = this.city
    )

    private fun RegisterRequest.toDomainRequest(): RegisterUserRequest? = RegisterUserRequest(
        firstName = this.firstName,
        secondName = this.secondName,
        birthdate = runCatching { LocalDate.parse(birthDate) }.getOrElse { return null },
        biography = biography,
        city = city,
        password = password
    )
}
