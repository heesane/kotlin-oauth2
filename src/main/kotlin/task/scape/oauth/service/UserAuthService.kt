package side.heesane.kopring.user.service

interface UserAuthService {
    fun register(email: String, password: String): String

    fun login(email: String, password: String): String

    fun logout(token: String): Boolean

    fun refreshToken(token: String): String
}