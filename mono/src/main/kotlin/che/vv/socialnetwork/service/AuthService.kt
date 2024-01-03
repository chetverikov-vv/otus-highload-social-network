package che.vv.socialnetwork.service

import che.vv.socialnetwork.controller.model.request.LoginRequest

interface AuthService {
    fun login(request: LoginRequest): LoginResult

    sealed class LoginResult {
        data class Success(val token: String) : LoginResult()
        object NotFound : LoginResult()
        object InvalidData : LoginResult()
        object InternalFailure : LoginResult()
    }
}