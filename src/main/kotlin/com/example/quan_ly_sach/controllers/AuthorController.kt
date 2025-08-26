package com.example.quan_ly_sach.controllers

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
    fun addAuthor(
        @Argument name: String,
        @Argument birthYear: Int?,
        @Argument nationality: String?
    ): AuthorEntity {
        val author = AuthorEntity(name = name, birthYear = birthYear, nationality = nationality)
        return authorRepo.save(author)
    }

    @MutationMapping
    fun updateAuthor(
        @Argument id: Long,
        @Argument name: String?,
        @Argument birthYear: Int?,
        @Argument nationality: String?
    ): AuthorEntity? {
        val author = authorRepo.findById(id).orElse(null) ?: return null
        name?.let { author.name = it }
        birthYear?.let { author.birthYear = it }
        nationality?.let { author.nationality = it }
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
