package side.heesane.kopring.user.domain

import jakarta.persistence.*
import side.heesane.kopring.global.enum.UserRole

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,

    @Column
    val refreshToken: String? = null
)