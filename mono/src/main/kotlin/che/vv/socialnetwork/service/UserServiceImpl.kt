package che.vv.socialnetwork.service

import che.vv.socialnetwork.controller.model.request.RegisterRequest
import che.vv.socialnetwork.dao.UserDao
import che.vv.socialnetwork.service.model.RegistrationUser
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class UserServiceImpl(
    private val userDao: UserDao
) : UserService {

    private val logger = KotlinLogging.logger {  }

    override fun findById(userId: String): UserService.FindResult {
        if (userId.isEmpty()) return UserService.FindResult.InvalidData
        val user = userDao.findById(userId).getOrElse {
            logger.error(it) { "Failure to find user" }
            return UserService.FindResult.InternalError
        }
            ?: return UserService.FindResult.NotFound
        return UserService.FindResult.Success(user)
    }

    override fun findByPrefixes(firstNamePrefix: String, lastNamePrefix: String): UserService.FindPrefixResult {
        if(firstNamePrefix.isBlank() || lastNamePrefix.isBlank()) return UserService.FindPrefixResult.InvalidData
        val users = userDao.findByPrefixes(firstNamePrefix, lastNamePrefix).getOrElse {
            logger.error(it) { "Failure to find users by first name prefix = $firstNamePrefix and last name prefix = $lastNamePrefix" }
            return UserService.FindPrefixResult.InternalError
        }
        return UserService.FindPrefixResult.Success(users)
    }

}
