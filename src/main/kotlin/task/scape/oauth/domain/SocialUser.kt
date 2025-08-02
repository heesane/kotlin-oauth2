package side.heesane.kopring.user.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import side.heesane.kopring.global.enum.OAuthProvider

@Table(name = "social_users")
@Entity
class SocialUser(
    name: String,
    email: String,
    @Enumerated(EnumType.STRING)
    private val provider: OAuthProvider,
    private val providerId: String
) : User(name = name, email = email)