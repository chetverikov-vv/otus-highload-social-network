package che.vv.socialnetwork.application

import che.vv.socialnetwork.domain.user.User
import che.vv.socialnetwork.domain.user.UserId
import che.vv.socialnetwork.domain.user.UserRepository
import che.vv.socialnetwork.domain.request.FindByPrefixRequest
import che.vv.socialnetwork.domain.request.RegisterUserRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    private val logger = KotlinLogging.logger { }
    override fun register(request: RegisterUserRequest): UserService.RegisterResult {
        val user = User.createFrom(request) ?: run {
            logger.error { "Failure ro transform request $request to user" }
            return UserService.RegisterResult.InvalidData
        }
        return userRepository.save(user).fold(
            onSuccess = {
                UserService.RegisterResult.Success(user.id)
            },
            onFailure = {
                logger.error(it) { "Failure to save user $user" }
                return UserService.RegisterResult.InternalError
            }
        )
    }

    override fun findById(userId: UserId): UserService.FindResult = userRepository.findById(userId).fold(
        onSuccess = {
            it?.let { UserService.FindResult.Success(it) } ?: UserService.FindResult.NotFound
        },
        onFailure = {
            logger.error(it) { "Failure to find user" }
            return UserService.FindResult.InternalError
        }
    )


    override fun findByPrefixes(findRequest: FindByPrefixRequest): UserService.FindPrefixResult =
        userRepository.findBy(findRequest).fold(
            onSuccess = {
                UserService.FindPrefixResult.Success(it)
            },
            onFailure = {
                logger.error(it) { "Failure to find users by $findRequest" }
                return UserService.FindPrefixResult.InternalError
            }
        )

}
