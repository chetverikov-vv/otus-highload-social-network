package che.vv.socialnetwork.domain.user

import java.util.*

@JvmInline
value class UserId private constructor(val value: String) {
    companion object{
        fun generate(): UserId = UserId(UUID.randomUUID().toString())
        fun fromString(rawId: String): UserId? = if(rawId.isNotEmpty()) UserId(rawId) else null
    }
}