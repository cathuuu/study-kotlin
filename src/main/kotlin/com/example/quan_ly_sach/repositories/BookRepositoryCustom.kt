package com.example.quan_ly_sach.repositories

import com.example.quan_ly_sach.dtos.Queries.AuthorQueryDto
import com.example.quan_ly_sach.dtos.Queries.BookQueryDto
import com.example.quan_ly_sach.entities.BookEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query


interface BookRepositoryCustom {
    fun findAllBooksPagedNavtive(query: BookQueryDto, pageable: Pageable): Page<BookEntity>
}