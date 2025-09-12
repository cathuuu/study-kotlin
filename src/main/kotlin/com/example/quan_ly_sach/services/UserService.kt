package com.example.quan_ly_sach.services

import com.example.quan_ly_sach.entities.UserEntity
import java.util.*

interface UserService : CommonService<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity
}