package com.example.quan_ly_sach.controllers

import com.example.quan_ly_sach.dtos.AuthorInput
import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.entities.BookEntity
import com.example.quan_ly_sach.repositories.AuthorRepository
import com.example.quan_ly_sach.repositories.BookRepository
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
    fun getAllAuthors(): List<AuthorEntity> = authorRepo.findAll()

    // ================= MUTATIONS =================
    @MutationMapping
    fun addAuthor(@Argument input: AuthorInput): AuthorEntity {
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
        @Argument input: AuthorInput
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

    // ================= RELATION =================
    @SchemaMapping(typeName = "Author", field = "books")
    fun books(author: AuthorEntity): List<BookEntity> {
        return bookRepo.findByAuthorId(author.id!!)
    }
}
