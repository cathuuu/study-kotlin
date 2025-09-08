package com.example.quan_ly_sach.config

import com.example.quan_ly_sach.components.JwtAuthenticationFilter
import com.example.quan_ly_sach.services.impl.UserDetailsServiceImpl
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
class SecurityConfig(
    private val customUserDetailsService: UserDetailsServiceImpl,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
    ) {

        @Bean
        fun filterChain(http: HttpSecurity): SecurityFilterChain {
            http
                .csrf { it.disable() }
                .authorizeHttpRequests {
                    it.requestMatchers("/graphql").permitAll()  // ✅ bắt buộc token
                    it.anyRequest().permitAll()
                }
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .httpBasic { it.disable() }
                .formLogin { it.disable() }

            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

            return http.build()
        }



    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager
}
