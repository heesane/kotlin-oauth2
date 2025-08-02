package side.heesane.kopring.global.auth

import com.fasterxml.jackson.annotation.JsonProperty
import side.heesane.kopring.global.enum.OAuthProvider
import side.heesane.kopring.user.domain.SocialUser

data class GoogleTokenResponse(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresIn: Int,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("refresh_token") val refreshToken: String?
)

data class GoogleUserInfo(
    val id: String,
    val email: String,
    val name: String,
    val picture: String
) {
    fun toUserEntity(): SocialUser {
        return SocialUser(
            name = this.name,
            email = this.email,
            provider = OAuthProvider.GOOGLE,
            providerId = this.id
        )
    }
}