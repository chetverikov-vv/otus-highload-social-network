package che.vv.socialnetwork.service

import che.vv.socialnetwork.controller.model.request.LoginRequest
import che.vv.socialnetwork.dao.TokenDao
import che.vv.socialnetwork.service.AuthService.LoginResult
import che.vv.socialnetwork.service.AuthService.LoginResult.*
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val encryptionService: EncryptionService,
    private val tokenDao: TokenDao
) : AuthService {

    private val logger = KotlinLogging.logger { }
    override fun login(request: LoginRequest): LoginResult {
        if (!request.isValid()) return InvalidData
        val encryptedPassword = encryptionService.encryptPassword(request.password)

        val token = tokenDao.find(request.id, encryptedPassword).getOrElse {
            logger.error(it) { "Failure to find token!" }
            return InternalFailure
        } ?: return NotFound

        return Success(token)
    }

    private fun LoginRequest.isValid(): Boolean = id.isNotEmpty() && password.isNotEmpty()
}