package com.example.quan_ly_sach.services

import com.example.quan_ly_sach.dtos.AuthenticationDto.AuthPayload
import com.example.quan_ly_sach.dtos.AuthenticationDto.LoginInput
import com.example.quan_ly_sach.dtos.AuthenticationDto.RegisterInput

interface AuthService {
    fun login(input : LoginInput): AuthPayload
    fun register(input: RegisterInput): AuthPayload
    fun refreshToken(refreshToken: String): AuthPayload
}