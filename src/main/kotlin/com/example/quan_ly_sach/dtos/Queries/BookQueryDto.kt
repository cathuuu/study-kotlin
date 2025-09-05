package com.example.quan_ly_sach.dtos.Queries

import com.example.quan_ly_sach.entities.AuthorEntity


data class BookQueryDto(
    var id: Long? = null,

    var title: String? = null,

    var publishedYear: Int? = null,

    var price: Double? = null,

    var quantity: Int? = null,

    var author: AuthorEntity? = null
) : QueryDto() {

}