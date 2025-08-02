package side.heesane.kopring.global.exception.code

import lombok.Getter
import org.springframework.http.HttpStatus

@Getter
enum class ExceptionCode(
    private val status: HttpStatus,
    private val code: String,
    private val message: String
) {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "사용자를 찾을 수 없습니다."),

}