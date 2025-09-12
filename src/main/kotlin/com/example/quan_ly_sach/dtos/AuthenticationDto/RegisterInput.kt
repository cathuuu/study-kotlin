package com.example.quan_ly_sach.dtos.AuthenticationDto

data class RegisterInput(
    val username: String,
    val password: String,
    val role: String
)
