package side.heesane.kopring.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import side.heesane.kopring.user.domain.User

@Repository
interface UserRepository : JpaRepository<User, Long> {

    // Optional과 동일
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    fun existsByName(name: String): Boolean

    fun findByName(name: String): User?

    @Query(
        """
        SELECT u
        FROM User u
        JOIN SocialUser su ON u.id = su.id
        WHERE su.providerId = :name AND su.provider = 'GOOGLE'
    """
    )
    fun findByGoogleId(@Param("googleId") googleId: String): User?
}