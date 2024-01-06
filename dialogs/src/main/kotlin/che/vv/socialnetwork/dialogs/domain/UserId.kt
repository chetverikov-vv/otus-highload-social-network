package che.vv.socialnetwork.dialogs.domain

@JvmInline
value class UserId private constructor(val value: String) {
    companion object {
        fun fromString(rawId: String): UserId? = if (rawId.isNotEmpty()) UserId(rawId) else null
    }
}