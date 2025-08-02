package side.heesane.kopring.user.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import side.heesane.kopring.user.service.GoogleOAuthService
import java.net.URI

@RestController
@RequestMapping("/api/v1/oauth2/google")
class GoogleOAuthController(
    private val googleOAuthService: GoogleOAuthService
) {

    @GetMapping("/login")
    fun redirectToGoogle(): ResponseEntity<Void> {
        val redirectUrl = googleOAuthService.buildGoogleAuthorizationUrl()
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(redirectUrl))
            .build()
    }

    @GetMapping("/callback")
    fun handleCallback(@RequestParam code: String): ResponseEntity<String> {
        val jwt = googleOAuthService.processOAuthLogin(code)
        // 프론트로 리디렉트하면서 JWT 포함
        val uri = URI.create("http://localhost:3000/oauth/success?token=$jwt")
        return ResponseEntity.status(HttpStatus.FOUND).location(uri).build()
    }
}