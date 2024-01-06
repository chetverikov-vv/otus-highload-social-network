package che.vv.socialnetwork.application

import che.vv.socialnetwork.domain.user.Credentials
import che.vv.socialnetwork.domain.user.Token

interface AuthService {
    fun login(credentials: Credentials): LoginResult

    sealed class LoginResult {
        data class Success(val token: Token) : LoginResult()
        object NotFound : LoginResult()
        object InternalFailure : LoginResult()
    }
}