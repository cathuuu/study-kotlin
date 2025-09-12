package com.example.quan_ly_sach.services

import com.example.quan_ly_sach.dtos.BookInputDto
import com.example.quan_ly_sach.dtos.BookPage
import com.example.quan_ly_sach.dtos.BookSearchInputDto
import com.example.quan_ly_sach.entities.BookEntity
import org.springframework.graphql.data.method.annotation.Argument

interface BookService : CommonService<BookEntity, Long> {
    fun getBookById(id: Long): BookEntity
    fun getAllBooks(): List<BookEntity>
    fun addBook(
        authorId: Long,
        input: BookInputDto
    ): BookEntity

    fun updateBook(
        id: Long,
        authorId: Long?,
        input: BookInputDto
    ): BookEntity

    fun deleteBook(id: Long): Boolean
    fun searchBooks(filter: BookSearchInputDto?): List<BookEntity>
    fun getBooksByPage(
        page: Int = 0,
        size: Int = 10
    ): BookPage



}