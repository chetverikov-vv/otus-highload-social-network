package che.vv.socialnetwork.domain.user

import java.util.*

@JvmInline
value class Token private constructor(val value: String){
    companion object{
        fun generate(): Token = Token(UUID.randomUUID().toString())
        fun fromString(rawToken: String): Token? = if(rawToken.isNotEmpty()) Token(rawToken) else null
    }
}