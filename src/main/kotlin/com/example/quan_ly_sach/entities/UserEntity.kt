package com.example.quan_ly_sach.entities

import com.example.quan_ly_sach.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    var username: String? = null,
    @Column(nullable = false)
    var password: String? = null,
    @Enumerated(EnumType.STRING)
    var role: Role = Role.READER
)

