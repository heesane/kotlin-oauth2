package side.heesane.kopring.global.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import side.heesane.kopring.user.domain.User
import side.heesane.kopring.user.repository.UserRepository

class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository
) : OncePerRequestFilter(){

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = resolveToken(request)

            if (token != null && jwtProvider.isValidToken(token)) {
                val userId = jwtProvider.getUserIdFromToken(token)
                val user = userRepository.findById(userId).orElseThrow {
                    IllegalArgumentException("해당 사용자 ID를 가진 사용자가 존재하지 않습니다.")
                }

                val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
                val authentication = UsernamePasswordAuthenticationToken(user, null, authorities)

                SecurityContextHolder.getContext().authentication = authentication
            }

            filterChain.doFilter(request, response)

        } catch (ex: Exception) {
            // 인증 오류 처리: 로그 남기고, 401 상태 반환 등
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Unauthorized: ${ex.message}")
        }

//        959122016387-l6l7ji68m71b98unh0hn89hu6rl2c0qd.apps.googleusercontent.com
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearer = request.getHeader("Authorization")
        return if (bearer != null && bearer.startsWith(("Bearer "))){
            bearer.substring(7)
        }
        else {
            throw IllegalArgumentException("Authorization 헤더가 잘못되었습니다.")
        }
    }

}