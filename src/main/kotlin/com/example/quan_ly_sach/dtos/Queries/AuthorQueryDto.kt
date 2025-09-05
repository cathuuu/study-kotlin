package com.example.quan_ly_sach.dtos.Queries

data class AuthorQueryDto(
    var id: Long? = null,
    val name: String? = null,
    val birthYear: Int? = null,
    val nationality: String? = null) : QueryDto() {

}