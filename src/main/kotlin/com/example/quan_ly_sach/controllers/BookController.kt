package com.example.quan_ly_sach.controllers

import com.example.quan_ly_sach.dtos.BookInputDto
import com.example.quan_ly_sach.dtos.BookPage
import com.example.quan_ly_sach.dtos.BookSearchInputDto
import com.example.quan_ly_sach.dtos.Queries.BookQueryDto
import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.entities.BookEntity
import com.example.quan_ly_sach.repositories.AuthorRepository
import com.example.quan_ly_sach.repositories.BookRepository
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

) {
    // ================= QUERIES =================
    @QueryMapping
    fun getBookById(@Argument id: Long): BookEntity? =
        bookRepo.findById(id).orElse(null)

    @QueryMapping
    fun getAllBooks(): List<BookEntity> = bookRepo.findAll()

    // ================= MUTATIONS =================
    @MutationMapping
    fun addBook(
        @Argument authorId: Long,
        @Argument input: BookInputDto
    ): BookEntity {
        val author = authorRepo.findById(authorId).orElseThrow {
            NoSuchElementException("Author with ID $authorId not found.")
        }
        val book = BookEntity(
            title = input.title,
            publishedYear = input.publishedYear,
            price = input.price,
            quantity = input.quantity,
            author = author
        )
        return bookRepo.save(book)
    }

    @MutationMapping
    fun updateBook(
        @Argument id: Long,
        @Argument authorId: Long?,
        @Argument input: BookInputDto
    ): BookEntity {
        val book = bookRepo.findById(id).orElseThrow {
            NoSuchElementException("Book with ID $id not found.")
        }

        // Cập nhật tác giả nếu có authorId mới
        authorId?.let {
            val newAuthor = authorRepo.findById(it).orElseThrow {
                NoSuchElementException("Author with ID $it not found.")
            }
            book.author = newAuthor
        }

        // Cập nhật các field từ input
        book.title = input.title
        book.publishedYear = input.publishedYear
        book.price = input.price
        book.quantity = input.quantity

        return bookRepo.save(book)
    }
    @QueryMapping
    fun searchBooks(@Argument filter: BookSearchInputDto?): List<BookEntity> {
        return bookRepo.searchBooks(
            title = "%${filter?.title?:""}%",
            publishedYear = filter?.publishedYear,
            minPrice = filter?.minPrice,
            maxPrice = filter?.maxPrice,
            minQuantity = filter?.minQuantity,
            maxQuantity = filter?.maxQuantity
        )
    }
    @QueryMapping
    fun getBooksByPage(
        @Argument page: Int = 0,
        @Argument size: Int = 10
    ): BookPage {
        val pageable = PageRequest.of(page, size)
        val result = bookRepo.findAll(pageable)
        return BookPage(
            content = result.content,
            totalElements = result.totalElements,
            totalPages = result.totalPages,
            number = result.number,
            size = result.size
        )
    }

    @MutationMapping
    fun deleteBook(@Argument id: Long): Boolean {
        return if (bookRepo.existsById(id)) {
            bookRepo.deleteById(id)
            true
        } else false
    }

    // ================= RELATION =================
    @SchemaMapping(typeName = "Book", field = "author")
    fun author(book: BookEntity): AuthorEntity? {
        return book.author
    }
}
