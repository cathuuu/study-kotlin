package com.example.quan_ly_sach.dtos

import com.example.quan_ly_sach.entities.BookEntity

data class BookPage(
    val content: List<BookEntity>,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int
)