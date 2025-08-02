package side.heesane.kopring.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import side.heesane.kopring.user.service.CustomUserDetailsService

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val customUserDetailsService: CustomUserDetailsService,
    private val passwordEncoder: PasswordEncoder
){

    @Bean
    fun securityFilterChain(http : HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }

            .authorizeHttpRequests {
                // 허용된 경로
                it.requestMatchers(
                    "/auth/**",
                    "/oauth2/**",
                    "/api/v1/users/register",
                    "/api/v1/users/login",
                    "/api/v1/oauth2/**",
                )
                    .permitAll()

                // 그 외의 모든 요청은 인증 필요
                it.anyRequest()
                    .authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 혹은 STATELESS for JWT
            }
            .oauth2Login {}
            .authenticationProvider(daoAuthenticationProvider())
            .build()
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider(customUserDetailsService)
        provider.setPasswordEncoder(passwordEncoder)
        return provider
    }

    @Bean
    fun authenticationManager(auth: AuthenticationConfiguration): AuthenticationManager =
        auth.authenticationManager
}