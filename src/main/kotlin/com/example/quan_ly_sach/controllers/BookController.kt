package com.example.quan_ly_sach.controllers

import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.entities.BookEntity
import com.example.quan_ly_sach.repositories.BookRepository
import com.example.quan_ly_sach.repositories.AuthorRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class BookController(
    private val bookRepo: BookRepository,
    private val authorRepo: AuthorRepository
) {

    @QueryMapping
    fun getAllBooks(): MutableList<BookEntity> = bookRepo.findAll()

    @QueryMapping
    fun getBookById(@Argument id: Long): BookEntity? = bookRepo.findById(id).orElse(null)

    @MutationMapping
    fun createBook(
        @Argument title: String,
        @Argument authorId: Long,
        @Argument publishedYear: Int?,
        @Argument price: Double?,
        @Argument quantity: Int?
    ): BookEntity? {
        val author = authorRepo.findById(authorId).orElse(null) ?: return null
        val book = BookEntity(title = title, publishedYear = publishedYear, price = price, quantity = quantity, authorId = author.id)
        return bookRepo.save(book)
    }

    @MutationMapping
    fun updateBook(
        @Argument id: Long,
        @Argument title: String?,
        @Argument publishedYear: Int?,
        @Argument price: Double?,
        @Argument quantity: Int?,
        @Argument authorId: Long?
    ): BookEntity? {
        val book = bookRepo.findById(id).orElse(null) ?: return null

        title?.let { book.title = it }
        publishedYear?.let { book.publishedYear = it }
        price?.let { book.price = it }
        quantity?.let { book.quantity = it }

        authorId?.let {
            val author = authorRepo.findById(it).orElse(null) ?: return null
            book.authorId = author.id
        }

        return bookRepo.save(book)
    }

    @MutationMapping
    fun deleteBook(@Argument id: Long): Boolean {
        return if (bookRepo.existsById(id)) {
            bookRepo.deleteById(id)
            true
        } else false
    }

    @SchemaMapping
    fun author(book: BookEntity): Long? {
        return book.authorId
    }
}
