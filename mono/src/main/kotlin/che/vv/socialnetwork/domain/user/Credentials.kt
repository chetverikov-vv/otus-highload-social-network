package che.vv.socialnetwork.domain.user

data class Credentials(
    val id: UserId,
    val password: EncryptedPassword
)
