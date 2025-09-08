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

@Controller
class AuthGraphQLController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @MutationMapping
    fun register(@Argument input: RegisterInput): Boolean {
        val user = UserEntity(
            username = input.username,
            password = passwordEncoder.encode(input.password),
            role = Role.valueOf(input.role)
        )
        userRepository.save(user)
        return true
    }

    @MutationMapping
    fun login(@Argument input: LoginInput): AuthPayload {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(input.username, input.password)
        )
        val user = userRepository.findByUsername(input.username)!!
        val token = jwtUtil.generateToken(user.username.toString(), user.role!!.name)
        return AuthPayload(token)
    }
}


data class AuthPayload(val token: String)