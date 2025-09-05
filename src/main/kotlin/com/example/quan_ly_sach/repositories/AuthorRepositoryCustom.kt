package com.example.quan_ly_sach.repositories

import com.example.quan_ly_sach.dtos.Queries.AuthorQueryDto
import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.entities.BookEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query


interface AuthorRepositoryCustom {
    fun findAllAuthorPagedNative(query: AuthorQueryDto, pageable: Pageable): Page<AuthorEntity>
}