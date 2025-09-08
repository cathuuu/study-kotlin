package com.example.quan_ly_sach.services.impl

import com.example.quan_ly_sach.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        val roleName = user.role?.name ?: "USER"

        return org.springframework.security.core.userdetails.User
            .withUsername(user.username ?: throw UsernameNotFoundException("Username is null"))
            .password(user.password ?: throw UsernameNotFoundException("Password is null"))
            .roles(roleName) // sẽ thành ROLE_USER, ROLE_ADMIN...
            .build()
    }
}
