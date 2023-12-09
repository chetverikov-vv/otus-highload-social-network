package che.vv.socialnetwork.controller

import che.vv.socialnetwork.controller.model.request.RegisterRequest
import che.vv.socialnetwork.controller.model.response.RegisterResponse
import che.vv.socialnetwork.controller.model.response.UserResponse
import che.vv.socialnetwork.service.UserService
import che.vv.socialnetwork.service.model.User
import com.opencsv.CSVReader
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.io.FileReader

@RestController()
@RequestMapping("user")
class UserController(
    private val userService: UserService
) {
    private val logger = KotlinLogging.logger { }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): RegisterResponse =
        when (val result = userService.register(registerRequest)) {
            is UserService.RegisterResult.Success -> RegisterResponse(result.userId)
            UserService.RegisterResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
            UserService.RegisterResult.InvalidData -> throw ResponseStatusException(BAD_REQUEST)
        }

    @GetMapping("/get/{id}")
    fun getById(@PathVariable id: String): UserResponse = when (val result = userService.findById(id)) {
        is UserService.FindResult.Success -> result.user.toResponse()
        UserService.FindResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
        UserService.FindResult.NotFound -> throw ResponseStatusException(HttpStatus.NOT_FOUND)
        UserService.FindResult.InvalidData -> throw ResponseStatusException(BAD_REQUEST)
    }

    @GetMapping("/search")
    fun searchByFirstNamePrefixAndLastNamePrefix(
        @RequestParam("first_name") firstNamePrefix: String,
        @RequestParam("last_name") lastNamePrefix: String
    ) = when (val result = userService.findByPrefixes(firstNamePrefix, lastNamePrefix)) {
        UserService.FindPrefixResult.InternalError -> throw ResponseStatusException(INTERNAL_SERVER_ERROR)
        UserService.FindPrefixResult.InvalidData -> throw ResponseStatusException(BAD_REQUEST)
        is UserService.FindPrefixResult.Success -> result.users.map { it.toResponse() }
    }

    @GetMapping("/load-test-data")
    fun loadTestData() {
        val csvData = ResourceUtils.getFile("classpath:test-users.csv")
        val csvReader = CSVReader(FileReader(csvData))
        val requests = csvReader.readAll().toRegisterRequests()
        requests.forEachIndexed { index, registerRequest ->
            register(registerRequest)
            logger.info { "Saved $index value" }
        }
    }

    private fun User.toResponse() = UserResponse(
        id = this.id,
        firstName = this.firstName,
        secondName = this.secondName,
        birthDate = this.birthdate.toString(),
        biography = this.biography,
        city = this.city
    )

    private fun List<Array<String>>.toRegisterRequests(): List<RegisterRequest> = this.map {
        RegisterRequest(
            firstName = it[1],
            secondName = it[0],
            birthDate = it[2],
            biography = it[3],
            city = it[4],
            password = "mypass"
        )
    }

}
