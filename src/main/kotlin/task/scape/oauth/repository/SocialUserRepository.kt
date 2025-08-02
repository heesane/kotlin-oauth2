package side.heesane.kopring.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import side.heesane.kopring.global.enum.OAuthProvider
import side.heesane.kopring.user.domain.SocialUser

@Repository
interface SocialUserRepository : JpaRepository<SocialUser,Long> {

    fun findByProviderAndProviderId(
        provider: OAuthProvider,
        providerId: String
    ): SocialUser?

}