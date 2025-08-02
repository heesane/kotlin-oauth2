package side.heesane.kopring.user.dto.request

data class LoginRequestDTO(
    private val email: String,
    private val password: String
) {
}