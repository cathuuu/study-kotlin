package com.example.quan_ly_sach.controllers

import com.example.quan_ly_sach.components.JwtUtil
import com.example.quan_ly_sach.dtos.LoginInput
import com.example.quan_ly_sach.dtos.RegisterInput
import com.example.quan_ly_sach.entities.UserEntity
import com.example.quan_ly_sach.repositories.UserRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import com.example.quan_ly_sach.enums.Role
import com.example.quan_ly_sach.services.impl.UserDetailsServiceImpl
import org.springframework.security.core.userdetails.UserDetails

@Controller
class AuthGraphQLController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsServiceImpl
) {

    @MutationMapping
    fun register(@Argument input: RegisterInput): Boolean {
        val user = UserEntity(
            username = input.username,
            password = passwordEncoder.encode(input.password),
            role = Role.valueOf(input.role.uppercase())
        )
        userRepository.save(user)
        return true
    }

    @MutationMapping
    fun login(@Argument input: LoginInput): AuthPayload {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(input.username, input.password)
        )
        val userDetails = auth.principal as UserDetails

        val accessToken = jwtUtil.generateAccessToken(userDetails.username, userDetails.authorities.first().authority)
        val refreshToken = jwtUtil.generateRefreshToken(userDetails.username)

        return AuthPayload(accessToken, refreshToken)
    }


    @MutationMapping
    fun refreshToken(@Argument refreshToken: String): AuthPayload {
        val username = jwtUtil.extractUsername(refreshToken)
        val userDetails = userDetailsService.loadUserByUsername(username)

        if (!jwtUtil.validateToken(refreshToken, userDetails.username)) {
            throw RuntimeException("Refresh token không hợp lệ hoặc đã hết hạn")
        }

        val user = userRepository.findByUsername(username)!!
        val accessToken = jwtUtil.generateAccessToken(user.username!!, user.role!!.name)
        val newRefreshToken = jwtUtil.generateRefreshToken(user.username!!)

        return AuthPayload(accessToken, newRefreshToken)
    }
}

data class AuthPayload(
    val accessToken: String,
    val refreshToken: String
)
