package com.example.quan_ly_sach.controllers

import com.example.quan_ly_sach.dtos.BookInputDto
import com.example.quan_ly_sach.dtos.BookPage
import com.example.quan_ly_sach.dtos.BookSearchInputDto
import com.example.quan_ly_sach.dtos.Queries.BookQueryDto
import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.entities.BookEntity
import com.example.quan_ly_sach.repositories.AuthorRepository
import com.example.quan_ly_sach.repositories.BookRepository
import com.example.quan_ly_sach.services.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller



@Controller
class BookController(
    private val bookRepo: BookRepository,
    private val authorRepo: AuthorRepository,
    private val bookService : BookService

) {
    // ================= QUERIES =================
    @QueryMapping
    fun getBookById(@Argument id: Long): BookEntity =
        bookService.getBookById(id)
    @QueryMapping
    fun getAllBooks(): List<BookEntity> = bookService.getAllBooks()

    // ================= MUTATIONS =================
    @MutationMapping
    fun addBook(
        @Argument authorId: Long,
        @Argument input: BookInputDto
    ): BookEntity {
        return bookService.addBook(authorId, input)
    }

    @MutationMapping
    fun updateBook(
        @Argument id: Long,
        @Argument authorId: Long?,
        @Argument input: BookInputDto
    ): BookEntity {
        return bookService.updateBook(id, authorId, input)
    }
    @QueryMapping
    fun searchBooks(@Argument filter: BookSearchInputDto?): List<BookEntity> {
        return bookService.searchBooks(filter)
    }
    @QueryMapping
    fun getBooksByPage(
        @Argument page: Int = 0,
        @Argument size: Int = 10
    ): BookPage {
       return bookService.getBooksByPage(page, size)
    }

    @MutationMapping
    fun deleteBook(@Argument id: Long): Boolean
    {
       return bookService.deleteBook(id)
    }

    // ================= RELATION =================
    @SchemaMapping(typeName = "Book", field = "author")
    fun author(book: BookEntity): AuthorEntity? {
        return book.author
    }
}
