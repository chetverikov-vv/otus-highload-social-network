package che.vv.socialnetwork.controller

import che.vv.socialnetwork.controller.model.response.UserResponse
import che.vv.socialnetwork.service.UserService
import che.vv.socialnetwork.service.model.User
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController()
class UserController(
    private val userService: UserService
) {
    private val logger = KotlinLogging.logger { }

    @GetMapping("/user/get/{id}")
    fun getById(@PathVariable id: String): UserResponse = when (val result = userService.findById(id)) {
        is UserService.FindResult.Success -> result.user.toResponse()
        UserService.FindResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
        UserService.FindResult.NotFound -> throw ResponseStatusException(HttpStatus.NOT_FOUND)
        UserService.FindResult.InvalidData -> throw ResponseStatusException(BAD_REQUEST)
    }

    @GetMapping("/user/search")
    fun searchByFirstNamePrefixAndLastNamePrefix(
        @RequestParam("first_name") firstNamePrefix: String,
        @RequestParam("last_name") lastNamePrefix: String
    ) = when (val result = userService.findByPrefixes(firstNamePrefix, lastNamePrefix)) {
        UserService.FindPrefixResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
        UserService.FindPrefixResult.InvalidData -> throw ResponseStatusException(BAD_REQUEST)
        is UserService.FindPrefixResult.Success -> result.users.map { it.toResponse() }
    }

    private fun User.toResponse() = UserResponse(
        id = this.id,
        firstName = this.firstName,
        secondName = this.secondName,
        birthDate = this.birthdate.toString(),
        biography = this.biography,
        city = this.city
    )
}
