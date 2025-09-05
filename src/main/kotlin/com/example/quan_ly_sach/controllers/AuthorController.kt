package com.example.quan_ly_sach.controllers

import com.example.quan_ly_sach.dtos.AuthorInputDto
import com.example.quan_ly_sach.dtos.AuthorPage
import com.example.quan_ly_sach.dtos.AuthorSearchInputDto
import com.example.quan_ly_sach.dtos.BookPage
import com.example.quan_ly_sach.dtos.Queries.AuthorQueryDto
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
class AuthorController(
    private val authorRepo: AuthorRepository,
    private val bookRepo: BookRepository
) {
    // ================= QUERIES =================
    @QueryMapping
    fun getAuthorById(@Argument id: Long): AuthorEntity? =
        authorRepo.findById(id).orElse(null)

    @QueryMapping
    fun getAuthorsByPage(
        @Argument page: Int = 0,
        @Argument size: Int = 10
    ): AuthorPage {
        val pageable = PageRequest.of(page, size)
        val result = authorRepo.findAll(pageable)
        return AuthorPage(
            content = result.content,
            totalElements = result.totalElements,
            totalPages = result.totalPages,
            number = result.number,
            size = result.size
        )
    }


    @QueryMapping
    fun getAllAuthors(): List<AuthorEntity> = authorRepo.findAll()

    // ================= MUTATIONS =================
    @MutationMapping
    fun addAuthor(@Argument input: AuthorInputDto): AuthorEntity {
        val author = AuthorEntity(
            name = input.name,
            birthYear = input.birthYear,
            nationality = input.nationality
        )
        return authorRepo.save(author)
    }

    @MutationMapping
    fun updateAuthor(
        @Argument id: Long,
        @Argument input: AuthorInputDto
    ): AuthorEntity {
        val author = authorRepo.findById(id).orElseThrow {
            NoSuchElementException("Author with ID $id not found.")
        }

        // Cập nhật các field từ input
        author.name = input.name
        author.birthYear = input.birthYear
        author.nationality = input.nationality

        return authorRepo.save(author)
    }

    @MutationMapping
    fun deleteAuthor(@Argument id: Long): Boolean {
        return if (authorRepo.existsById(id)) {
            authorRepo.deleteById(id)
            true
        } else false
    }

    @QueryMapping
    fun searchAuthors(@Argument filter: AuthorSearchInputDto?): List<AuthorEntity> {
        return authorRepo.searchAuthors(
            keyword = filter?.keyword,
            nationality = filter?.nationality,
            birthYear = filter?.birthYear
        )
    }
    // ================= RELATION =================
    @SchemaMapping(typeName = "Author", field = "books")
    fun books(author: AuthorEntity): List<BookEntity> {
        return bookRepo.findByAuthorId(author.id!!)
    }
}
