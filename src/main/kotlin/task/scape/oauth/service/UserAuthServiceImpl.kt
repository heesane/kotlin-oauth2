package side.heesane.kopring.user.service

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import side.heesane.kopring.global.auth.JwtProvider
import side.heesane.kopring.user.domain.NormalUser
import side.heesane.kopring.user.repository.NormalUserRepository

@Service
class UserAuthServiceImpl(
    private val normalUserRepository: NormalUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
) : UserAuthService {

    @Transactional
    override fun register(email: String, password: String): String {

        if(normalUserRepository.existsByEmail(email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }

        val encodedPassword = passwordEncoder.encode(password)

        val user = NormalUser(
            name = email.split("@")[0],
            email = email,
            password = encodedPassword
        )

        normalUserRepository.save(user)

        return user.email
    }

    override fun login(email: String, password: String): String {
        val user = normalUserRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다.")

        if (!passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("비밀번호가 일치하지 않습니다.")
        }

        return jwtProvider.generateToken(user) // JWT 토큰 발급
    }

    override fun logout(token: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun refreshToken(token: String): String {
        TODO("Not yet implemented")
    }
}