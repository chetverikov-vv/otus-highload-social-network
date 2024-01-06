package che.vv.socialnetwork.dialogs.domain

interface UserRepository {

    fun findIdBy(token: Token): Result<UserId?>
}