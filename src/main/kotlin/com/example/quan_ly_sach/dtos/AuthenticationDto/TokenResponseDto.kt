package com.example.quan_ly_sach.dtos.AuthenticationDto

data class TokenResponseDto(
    val accessToken: String,
    val refreshToken: String
)
