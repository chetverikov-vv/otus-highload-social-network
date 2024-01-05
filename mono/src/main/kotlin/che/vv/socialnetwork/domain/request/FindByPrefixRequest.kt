package che.vv.socialnetwork.domain.request

data class FindByPrefixRequest private constructor(
    val firstNamePrefix: String,
    val lastNamePrefix: String
) {
    companion object {
        fun from(rawFirstNamePrefix: String, rawLastNamePrefix: String): FindByPrefixRequest? =
            if (rawFirstNamePrefix.isNotEmpty() && rawLastNamePrefix.isNotEmpty()) FindByPrefixRequest(
                firstNamePrefix = rawFirstNamePrefix,
                lastNamePrefix = rawLastNamePrefix
            ) else null
    }
}
