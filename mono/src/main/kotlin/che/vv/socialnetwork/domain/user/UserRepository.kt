package che.vv.socialnetwork.domain.user

import che.vv.socialnetwork.domain.request.FindByPrefixRequest

interface UserRepository {
    fun save(user: User): Result<Unit>
    fun findById(id: UserId): Result<User?>
    fun findBy(request: FindByPrefixRequest): Result<List<User>>

    fun findTokenBy(credentials: Credentials):Result<Token?>

    fun findIdBy(token: Token): Result<UserId?>
}