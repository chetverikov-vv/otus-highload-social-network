package che.vv.socialnetwork.dao



interface TokenDao {
    fun find(userId: String, encryptedPassword: String):Result<String?>
}