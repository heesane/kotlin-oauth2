package side.heesane.kopring.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "normal_users")
class NormalUser(

    name: String,

    email: String,

    @Column(nullable = false)
    val password: String

) : User(name = name, email = email)