package che.vv.socialnetwork.controller

import che.vv.socialnetwork.controller.model.request.RegisterRequest
import che.vv.socialnetwork.controller.model.response.RegisterResponse
import che.vv.socialnetwork.controller.model.response.UserResponse
import che.vv.socialnetwork.service.UserService
import che.vv.socialnetwork.service.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController()
@RequestMapping("user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): RegisterResponse =
        when (val result = userService.register(registerRequest)) {
            is UserService.RegisterResult.Success -> RegisterResponse(result.userId)
            UserService.RegisterResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
            UserService.RegisterResult.InvalidData -> throw ResponseStatusException(BAD_REQUEST)
        }

    @GetMapping("/get/{id}")
    fun getById(@PathVariable id: String): UserResponse = when(val result = userService.findById(id)){
        is UserService.FindResult.Success -> result.user.toResponse()
        UserService.FindResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
        UserService.FindResult.NotFound -> throw ResponseStatusException(HttpStatus.NOT_FOUND)
        UserService.FindResult.InvalidData -> throw ResponseStatusException(BAD_REQUEST)
    }

    private fun User.toResponse() = UserResponse(
        firstName = this.firstName,
        secondName = this.secondName,
        birthDate = this.birthdate.toString(),
        biography = this.biography,
        city = this.city
    )

}