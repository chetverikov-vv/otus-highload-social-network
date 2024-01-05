package che.vv.socialnetwork.domain.user

import java.security.MessageDigest

@JvmInline
value class EncryptedPassword (val value: String){
    companion object{
        private val md: MessageDigest = MessageDigest.getInstance("SHA-256")
        private const val hexChars = "0123456789ABCDEF"
        private const val passwordSalt = "otus2023"
        fun fromRawPassword(rawPassword: String): EncryptedPassword? = if(rawPassword.isNotEmpty()) encrypt(rawPassword) else null
        fun fromString(password: String): EncryptedPassword? = if(password.isNotEmpty()) EncryptedPassword(password) else null
        private fun encrypt(rawPassword: String): EncryptedPassword {
            val digest = md.digest((rawPassword + passwordSalt).toByteArray())
            val result = StringBuilder(digest.size * 2)

            digest.forEach {
                val i = it.toInt()
                result.append(hexChars[i shr 4 and 0x0f])
                result.append(hexChars[i and 0x0f])
            }

            return EncryptedPassword(result.toString())
        }
    }
}