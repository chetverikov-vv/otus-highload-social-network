package che.vv.socialnetwork.application

import che.vv.socialnetwork.domain.user.Credentials
import che.vv.socialnetwork.domain.user.Token
import che.vv.socialnetwork.domain.user.UserId

interface AuthService {
    fun login(credentials: Credentials): LoginResult
    fun me(token: Token): MeResult

    sealed class LoginResult {
        data class Success(val token: Token) : LoginResult()
        object NotFound : LoginResult()
        object InternalFailure : LoginResult()
    }

    sealed class MeResult {
        data class Success(val id: UserId) : MeResult()
        object NotFound : MeResult()
        object InternalFailure : MeResult()
    }
}