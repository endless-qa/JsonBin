/**
 * A place for common functionality regarding authentication
 */
object AuthUtil {
    /**
     * Gets API token (Master-Key) from the system properties
     * @return the API token that can be used for further API requests
     * @throws AuthorisationRequiredException if apiToken property wasn't found
     */
    fun getToken(): String {
        val token = System.getProperty("apiToken")
        if (token.isNullOrEmpty()) {
            throw AuthorisationRequiredException("A valid API token must be provided as a command-line option or system property")
        } else return token
    }
}

/**
 * Custom exception that is thrown when API token cannot be found
 */
class AuthorisationRequiredException(message: String) : Exception(message)