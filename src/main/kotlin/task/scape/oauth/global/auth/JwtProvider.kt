package side.heesane.kopring.global.auth

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import side.heesane.kopring.user.domain.User
import side.heesane.kopring.user.repository.UserRepository
import java.security.Key
import java.util.*


@Component
class JwtProvider(
    @Value("\${spring.jwt.secret.key}") private val secretKey: String,
    @Value("\${spring.jwt.secret.expiration}") private val accessTokenExpireTime: Long,
    private val userRepository: UserRepository
) {

    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())
    private val jwtParser: JwtParser = Jwts.parserBuilder().setSigningKey(key).build()

    fun generateToken(user: User): String {
        val now = Date()
        val expiry = Date(now.time + accessTokenExpireTime)

        return Jwts.builder()
            .setSubject(user.id.toString())
            .claim("email", user.email)
            .claim("role", user.role.name)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUserIdFromToken(token: String): Long {
        return jwtParser.parseClaimsJws(token).body.subject.toLong()
    }

    fun isValidToken(token: String): Boolean {
        return try {
            jwtParser.parseClaimsJws(token)
            true
        } catch (e: JwtException) {
            false
        }
    }

    fun getAuthentication(token: String): Authentication {
        val userId = getUserIdFromToken(token)

        val user = userRepository.findById(userId).orElseThrow {
            UsernameNotFoundException("User not found for ID: $userId")
        }

        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))

        return UsernamePasswordAuthenticationToken(user, null, authorities)
    }
}