package che.vv.socialnetwork.service

import che.vv.socialnetwork.controller.model.request.LoginRequest
import che.vv.socialnetwork.controller.model.request.RegisterRequest

interface AuthService {
    fun login(request: LoginRequest): LoginResult
    fun register(request: RegisterRequest): RegisterResult

    sealed class LoginResult {
        data class Success(val token: String) : LoginResult()
        object NotFound : LoginResult()
        object InvalidData : LoginResult()
        object InternalFailure : LoginResult()
    }

    sealed class RegisterResult{
        data class Success(val userId: String):RegisterResult()
        object InvalidData: RegisterResult()
        object InternalError: RegisterResult()
    }
}