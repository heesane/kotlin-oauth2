package side.heesane.kopring.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import side.heesane.kopring.user.domain.NormalUser

@Repository
interface NormalUserRepository : JpaRepository<NormalUser, Long> {

    fun findByEmail(email: String): NormalUser?

    fun existsByEmail(email: String): Boolean

}