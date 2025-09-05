package com.example.quan_ly_sach.dtos

import com.example.quan_ly_sach.entities.AuthorEntity

data class AuthorPage (
    val content: List<AuthorEntity>,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int
)