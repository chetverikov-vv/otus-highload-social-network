package che.vv.socialnetwork.application

import che.vv.socialnetwork.domain.user.User
import che.vv.socialnetwork.domain.user.UserId
import che.vv.socialnetwork.domain.request.FindByPrefixRequest
import che.vv.socialnetwork.domain.request.RegisterUserRequest

interface UserService {
    fun register(request: RegisterUserRequest): RegisterResult
    fun findById(userId: UserId):FindResult
    fun findByPrefixes(findRequest: FindByPrefixRequest):FindPrefixResult

    sealed class RegisterResult{
        data class Success(val userId: UserId):RegisterResult()
        object InvalidData: RegisterResult()
        object InternalError: RegisterResult()
    }

    sealed class FindResult{
        data class Success(val user: User):FindResult()
        object InvalidData:FindResult()
        object NotFound: FindResult()
        object InternalError: FindResult()
    }

    sealed class FindPrefixResult{
        data class Success(val users: List<User>):FindPrefixResult()
        object InternalError: FindPrefixResult()
    }
}