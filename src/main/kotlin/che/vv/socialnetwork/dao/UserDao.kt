package che.vv.socialnetwork.dao

import che.vv.socialnetwork.service.model.RegistrationUser
import che.vv.socialnetwork.service.model.User

interface UserDao {
    fun register(user: RegistrationUser ): Result<Unit>
    fun findById(userId: String): Result<User?>
}