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

    private val secretKey = "rkli1roo2EJZfRFLJb7Zu0BDi/J8w8lcOXuNtuilwbQ=" // 256-bit base64 key
    private val key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey))

    private val accessTokenExpirationMs = 15 * 60 * 1000   // 15 phút
    private val refreshTokenExpirationMs = 7 * 24 * 60 * 60 * 1000 // 7 ngày

    fun generateAccessToken(username: String, role: String): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenExpirationMs)

        return Jwts.builder()
            .setSubject(username)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshTokenExpirationMs)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUsername(token: String): String =
        extractAllClaims(token).subject

    fun extractRole(token: String): String =
        extractAllClaims(token).get("role", String::class.java)

    fun validateToken(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return extractedUsername == username && !isTokenExpired(token)
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

