package com.example.quan_ly_sach.dtos

data class AuthorSearchInput (
    val keyword: String? = null,
    val nationality: String? = null,
    val birthYear: Int? = null
)