package com.example.quan_ly_sach.services

import com.example.quan_ly_sach.dtos.AuthorInputDto
import com.example.quan_ly_sach.dtos.AuthorPage
import com.example.quan_ly_sach.dtos.AuthorSearchInputDto
import com.example.quan_ly_sach.entities.AuthorEntity
import org.springframework.graphql.data.method.annotation.Argument

interface AuthorService : CommonService<AuthorEntity, Long>{
    fun getAuthorById( id: Long): AuthorEntity?
    fun getAllAuthors(): List<AuthorEntity>
    fun getAuthorsByPage(
        page: Int = 0,
        size: Int = 10
    ): AuthorPage
    fun addAuthor(input: AuthorInputDto): AuthorEntity
    fun updateAuthor(
        id: Long,
        input: AuthorInputDto
    ): AuthorEntity
    fun deleteAuthor(id: Long): Boolean
    fun searchAuthors(filter: AuthorSearchInputDto?): List<AuthorEntity>
}