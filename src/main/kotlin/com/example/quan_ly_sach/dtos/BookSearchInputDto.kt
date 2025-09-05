package com.example.quan_ly_sach.dtos

data class BookSearchInputDto(
    val title: String? = null,
    val publishedYear: Int? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minQuantity: Int? = null,
    val maxQuantity: Int? = null
)
