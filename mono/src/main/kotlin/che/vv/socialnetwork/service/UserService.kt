package che.vv.socialnetwork.service

import che.vv.socialnetwork.controller.model.request.RegisterRequest
import che.vv.socialnetwork.service.model.User

interface UserService {
    fun findById(userId: String):FindResult
    fun findByPrefixes(firstNamePrefix: String, lastNamePrefix: String):FindPrefixResult

    sealed class FindResult{
        data class Success(val user: User):FindResult()
        object InvalidData:FindResult()
        object NotFound: FindResult()
        object InternalError: FindResult()
    }

    sealed class FindPrefixResult{
        data class Success(val users: List<User>):FindPrefixResult()
        object InvalidData:FindPrefixResult()
        object InternalError: FindPrefixResult()
    }
}