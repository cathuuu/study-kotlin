package com.example.quan_ly_sach.dtos

import com.example.quan_ly_sach.enums.Role


data class UserDto (
    val id: Long? = null,

    var username: String? = null,

    var role: Role = Role.READER
)