package che.vv.socialnetwork.service

import che.vv.socialnetwork.controller.model.request.LoginRequest
import che.vv.socialnetwork.controller.model.request.RegisterRequest
import che.vv.socialnetwork.dao.AuthDao
import che.vv.socialnetwork.service.AuthService.LoginResult
import che.vv.socialnetwork.service.AuthService.LoginResult.*
import che.vv.socialnetwork.service.AuthService.RegisterResult
import che.vv.socialnetwork.service.model.RegistrationUser
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AuthServiceImpl(
    private val encryptionService: EncryptionService,
    private val authDao: AuthDao
) : AuthService {

    private val logger = KotlinLogging.logger { }
    override fun login(request: LoginRequest): LoginResult {
        if (!request.isValid()) return InvalidData
        val encryptedPassword = encryptionService.encryptPassword(request.password)

        val token = authDao.find(request.id, encryptedPassword).getOrElse {
            logger.error(it) { "Failure to find token!" }
            return InternalFailure
        } ?: return NotFound

        return Success(token)
    }

    override fun register(request: RegisterRequest): RegisterResult {
        val registrationUser = request.validateAndTransform() ?: return RegisterResult.InvalidData
        authDao.register(registrationUser).getOrElse {
            logger.error(it) { "Failure to register user" }
            return RegisterResult.InternalError
        }
        return RegisterResult.Success(registrationUser.id)
    }

    private fun LoginRequest.isValid(): Boolean = id.isNotEmpty() && password.isNotEmpty()

    private fun RegisterRequest.validateAndTransform(): RegistrationUser? {
        return RegistrationUser(
            id = encryptionService.generateUserId(),
            firstName = this.firstName,
            secondName = this.secondName,
            birthdate = runCatching { LocalDate.parse(birthDate) }.getOrElse { return null },
            biography = this.biography,
            city = this.city,
            encryptedPassword = if (password.isEmpty()) return null else encryptionService.encryptPassword(password),
            token = encryptionService.generateToken()
        )
    }
}