package che.vv.socialnetwork.dao

import che.vv.socialnetwork.service.model.RegistrationUser


interface AuthDao {
    fun find(userId: String, encryptedPassword: String):Result<String?>
    fun register(user: RegistrationUser): Result<Unit>
}