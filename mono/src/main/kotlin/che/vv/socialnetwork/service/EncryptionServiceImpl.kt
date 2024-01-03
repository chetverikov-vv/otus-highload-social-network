package che.vv.socialnetwork.service

import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.UUID

@Service
class EncryptionServiceImpl : EncryptionService {
    private val md: MessageDigest = MessageDigest.getInstance("SHA-256")
    private val hexChars = "0123456789ABCDEF"
    private val passwordSalt = "otus2023"
    override fun encryptPassword(rawPassword: String): String {
        val digest = md.digest((rawPassword + passwordSalt).toByteArray())
        val result = StringBuilder(digest.size * 2)

        digest.forEach {
            val i = it.toInt()
            result.append(hexChars[i shr 4 and 0x0f])
            result.append(hexChars[i and 0x0f])
        }

        return result.toString()
    }

    override fun generateToken(): String = UUID.randomUUID().toString()
    override fun generateUserId(): String = UUID.randomUUID().toString()
}