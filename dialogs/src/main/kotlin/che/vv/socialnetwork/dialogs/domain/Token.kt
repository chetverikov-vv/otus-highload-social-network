package che.vv.socialnetwork.dialogs.domain

@JvmInline
value class Token private constructor(val value: String) {
    companion object {
        fun fromString(rawToken: String): Token? = if (rawToken.isNotEmpty()) Token(rawToken) else null
    }
}