package che.vv.socialnetwork.dialogs.presentation.model

import che.vv.socialnetwork.dialogs.domain.Token

data class BearerHeader private constructor(val token: Token) {
    companion object {
        private const val BEARER_HEADER_PREFIX = "Bearer"
        fun fromHeaderString(headerString: String): BearerHeader? {
            val separatedHeader = headerString.split(" ")
            if (separatedHeader.size != 2) return null
            if (separatedHeader[0] != BEARER_HEADER_PREFIX) return null
            val token = Token.fromString(separatedHeader[1]) ?: return null
            return BearerHeader(token)
        }
    }
}
