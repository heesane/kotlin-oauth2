package side.heesane.kopring.global.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GoogleOAuthClient(
    @Value("\${spring.security.oauth2.client.registration.google.client-id}") private val clientId: String,
    @Value("\${spring.security.oauth2.client.registration.google.client-secret}") private val clientSecret: String,
    @Value("\${spring.security.oauth2.client.registration.google.redirect-uri}") private val redirectUri: String,
    private val webClient: WebClient,
) {

    fun getAccessToken(code: String): GoogleTokenResponse {
        webClient.post()

        val response = webClient
            .post()
            .uri("https://oauth2.googleapis.com/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(
                "code=$code" +
                        "&client_id=${clientId}" +
                        "&client_secret=${clientSecret}" +
                        "&redirect_uri=${redirectUri}" +
                        "&grant_type=authorization_code"
            )
            .retrieve()
            .bodyToMono(GoogleTokenResponse::class.java)
            .block() ?: throw RuntimeException("토큰 요청 실패")

        return response
    }

    fun getUserInfo(accessToken: String): GoogleUserInfo {
        return webClient.get()
            .uri("https://www.googleapis.com/oauth2/v2/userinfo")
            .headers { it.setBearerAuth(accessToken) }
            .retrieve()
            .bodyToMono(GoogleUserInfo::class.java)
            .block() ?: throw RuntimeException("유저 정보 가져오기 실패")
    }
}