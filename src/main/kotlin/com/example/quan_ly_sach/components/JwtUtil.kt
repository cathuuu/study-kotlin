package com.example.quan_ly_sach.components

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    // Nên đặt trong application.properties rồi inject vào
    private val secretKey = "rkli1roo2EJZfRFLJb7Zu0BDi/J8w8lcOXuNtuilwbQ=" // 256-bit base64 key
    private val expirationMs = 86400000 // 1 ngày

    private val key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey))

    fun generateToken(username: String, role: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMs)

        return Jwts.builder()
            .setSubject(username)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUsername(token: String): String =
        extractAllClaims(token).subject

    fun extractRole(token: String): String =
        extractAllClaims(token).get("role", String::class.java)

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean =
        extractAllClaims(token).expiration.before(Date())

    private fun extractAllClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}
