package side.heesane.kopring.user.service

interface GoogleOAuthService {

    fun buildGoogleAuthorizationUrl(): String

    fun processOAuthLogin(code: String): String

}
