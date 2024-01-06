package che.vv.socialnetwork.application

import che.vv.socialnetwork.domain.user.Credentials
import che.vv.socialnetwork.domain.user.UserRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository
) : AuthService {

    private val logger = KotlinLogging.logger { }
    override fun login(credentials: Credentials): AuthService.LoginResult =
        userRepository.findTokenBy(credentials).fold(
            onSuccess = { it?.let { AuthService.LoginResult.Success(it) } ?: AuthService.LoginResult.NotFound },
            onFailure = {
                logger.error(it) { "Failure to find token for ${credentials.id}" }
                AuthService.LoginResult.InternalFailure
            }
        )
}