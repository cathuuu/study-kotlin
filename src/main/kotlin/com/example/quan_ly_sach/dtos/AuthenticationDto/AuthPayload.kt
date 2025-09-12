package com.example.quan_ly_sach.dtos.AuthenticationDto

data class AuthPayload(
    val accessToken: String,
    val refreshToken: String
)
