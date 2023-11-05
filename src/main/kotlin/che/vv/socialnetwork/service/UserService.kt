package che.vv.socialnetwork.service

import che.vv.socialnetwork.controller.model.request.RegisterRequest
import che.vv.socialnetwork.service.model.User

interface UserService {
    fun register(request: RegisterRequest): RegisterResult
    fun findById(userId: String):FindResult

    sealed class RegisterResult{
        data class Success(val userId: String):RegisterResult()
        object InvalidData: RegisterResult()
        object InternalError: RegisterResult()
    }

    sealed class FindResult{
        data class Success(val user: User):FindResult()
        object InvalidData:FindResult()
        object NotFound: FindResult()
        object InternalError: FindResult()
    }
}