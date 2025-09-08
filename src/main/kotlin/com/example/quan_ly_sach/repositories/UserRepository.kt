package com.example.quan_ly_sach.repositories

import com.example.quan_ly_sach.entities.UserEntity
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
}