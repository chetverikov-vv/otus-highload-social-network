package che.vv.socialnetwork.service

interface EncryptionService {
    fun encryptPassword(rawPassword: String): String
    fun generateToken(): String
    fun generateUserId(): String
}