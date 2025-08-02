package side.heesane.kopring.user.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import side.heesane.kopring.user.dto.request.UserRegisterRequest
import side.heesane.kopring.user.service.UserAuthService

@RequestMapping("/api/v1/users")
@RestController
class UserController(
    private val userAuthService: UserAuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: UserRegisterRequest): ResponseEntity<String> {
        val email = userAuthService.register(request.email, request.password)
        return ResponseEntity.ok(email)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: UserRegisterRequest): ResponseEntity<String> {
        val token = userAuthService.login(request.email, request.password)
        return ResponseEntity.ok(token)
    }
}