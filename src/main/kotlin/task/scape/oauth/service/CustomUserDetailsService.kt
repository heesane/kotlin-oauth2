package side.heesane.kopring.user.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import side.heesane.kopring.user.domain.CustomUserDetails
import side.heesane.kopring.user.repository.NormalUserRepository

@Service
class CustomUserDetailsService (
    private val normalUserRepository: NormalUserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String) : UserDetails {
        return normalUserRepository.findByEmail(username)
            ?.let { CustomUserDetails(it) }
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
    }
}