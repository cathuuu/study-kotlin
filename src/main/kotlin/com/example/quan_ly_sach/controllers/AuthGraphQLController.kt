package com.example.quan_ly_sach.controllers

import com.example.quan_ly_sach.components.JwtUtil
import com.example.quan_ly_sach.dtos.AuthenticationDto.AuthPayload
import com.example.quan_ly_sach.dtos.AuthenticationDto.LoginInput
import com.example.quan_ly_sach.dtos.AuthenticationDto.RegisterInput
import com.example.quan_ly_sach.entities.UserEntity
import com.example.quan_ly_sach.repositories.UserRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import com.example.quan_ly_sach.enums.Role
import com.example.quan_ly_sach.services.AuthService
import com.example.quan_ly_sach.services.UserService
import com.example.quan_ly_sach.services.impl.UserDetailsServiceImpl
import org.springframework.security.core.userdetails.UserDetails

@Controller
class AuthGraphQLController(
    private val authService: AuthService,
) {
    @MutationMapping
    fun register(@Argument input: RegisterInput): AuthPayload {
        return authService.register(input)
    }

    @MutationMapping
    fun login(@Argument input: LoginInput): AuthPayload {
        return authService.login(input)
    }

    @MutationMapping
    fun refreshToken(@Argument refreshToken: String): AuthPayload {
        return authService.refreshToken(refreshToken)
    }
}


