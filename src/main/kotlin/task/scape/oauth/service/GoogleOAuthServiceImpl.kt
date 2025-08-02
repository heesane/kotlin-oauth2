package side.heesane.kopring.user.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import side.heesane.kopring.global.auth.GoogleOAuthClient
import side.heesane.kopring.global.auth.JwtProvider
import side.heesane.kopring.user.repository.UserRepository
import java.net.URLEncoder

@Service
class GoogleOAuthServiceImpl(
    private val googleOAuthClient: GoogleOAuthClient,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    @Value("\${spring.security.oauth2.client.registration.google.client-id}") private val clientId: String,
    @Value("\${spring.security.oauth2.client.registration.google.client-secret}") private val clientSecret: String,
    @Value("\${spring.security.oauth2.client.registration.google.redirect-uri}") private val redirectUri: String
) : GoogleOAuthService {
    override fun buildGoogleAuthorizationUrl(): String {
        val params = mapOf(
            "client_id" to clientId,
            "redirect_uri" to redirectUri,
            "response_type" to "code",
            "scope" to "openid email profile",
            "access_type" to "offline",
            "prompt" to "consent"
        )

        val query = params.map { "${it.key}=${URLEncoder.encode(it.value, "UTF-8")}" }
            .joinToString("&")

        return "https://accounts.google.com/o/oauth2/v2/auth?$query"
    }

    override fun processOAuthLogin(code: String): String {
        val token = googleOAuthClient.getAccessToken(code)
        val userInfo = googleOAuthClient.getUserInfo(token.accessToken)

        val user = userRepository.findByGoogleId(userInfo.id)
            ?: userRepository.save(userInfo.toUserEntity())

        return jwtProvider.generateToken(user)
    }
}