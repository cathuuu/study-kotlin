package com.example.quan_ly_sach.services.impl

import com.example.quan_ly_sach.components.JwtUtil
import com.example.quan_ly_sach.dtos.AuthenticationDto.AuthPayload
import com.example.quan_ly_sach.dtos.AuthenticationDto.LoginInput
import com.example.quan_ly_sach.dtos.AuthenticationDto.RegisterInput
import com.example.quan_ly_sach.entities.UserEntity
import com.example.quan_ly_sach.enums.Role
import com.example.quan_ly_sach.repositories.UserRepository
import com.example.quan_ly_sach.services.AuthService
import com.example.quan_ly_sach.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImp(
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val jwtToken : JwtUtil,
    @Autowired
    private val passwordEncoder: PasswordEncoder,
    @Autowired
    private val userService: UserService,
    @Autowired
    private val userRepository: UserRepository

) : AuthService {
    override fun login(input: LoginInput) : AuthPayload {
        val authentication = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(input.username, input.password)
            )
        } catch (ex: Exception) {
            throw RuntimeException("Invalid username or password")
        }

        // 2. Lấy user đã xác thực
        val user = userService.findByUsername(input.username)

        // 3. Tạo accessToken & refreshToken
        val accessToken = jwtToken.generateAccessToken(user.username ?: "", user.role.name)
        val refreshToken = jwtToken.generateRefreshToken(user.username ?: "")


        // 4. Trả về theo đúng GraphQL schema
        return AuthPayload(
            accessToken = accessToken,
            refreshToken = refreshToken
        )

    }

    override fun register(input: RegisterInput) : AuthPayload {
        // 1. Check user đã tồn tại chưa
        if (userService.findByUsername(input.username) != null) {
            throw IllegalArgumentException("Username already exists")
        }

        // 2. Encode password
        val encodedPassword = passwordEncoder.encode(input.password)

        // 3. Tạo user mới
        val newUser = UserEntity(
            username = input.username,
            password = encodedPassword,
            role = Role.READER
        )

        userService.save(newUser)

        // 4. Authenticate lại user
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(input.username, input.password)
        )

        // 5. Sinh token
        val accessToken = jwtToken.generateAccessToken(input.username, input.role)
        val refreshToken = jwtToken.generateRefreshToken(input.username)

        return AuthPayload(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    override fun refreshToken(refreshToken: String): AuthPayload {
        val username = jwtToken.validateRefreshToken(refreshToken)
            ?: throw RuntimeException("Invalid refresh token")

        // 2. Lấy user từ DB
        val user = userRepository.findByUsername(username)
            ?: throw RuntimeException("User not found")

        // 3. Sinh accessToken mới
        val accessToken = jwtToken.generateAccessToken(user.username.toString(), user.role.name)

        // 4. (Tuỳ chọn) sinh refreshToken mới
        val newRefreshToken = jwtToken.generateRefreshToken(user.username.toString())

        return AuthPayload(accessToken = accessToken, refreshToken = newRefreshToken)
    }
}