package side.heesane.kopring.user.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val user: NormalUser
) : UserDetails {
    override fun getAuthorities() : Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("Role_${user.role.name}"))

    override fun getPassword(): String = user.password
    override fun getUsername(): String = user.email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    fun getUser(): NormalUser = user
}