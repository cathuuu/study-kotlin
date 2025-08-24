package com.example.quan_ly_sach.controllers

import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.repositories.AuthorRepository
import com.example.quan_ly_sach.repositories.BookRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.awt.print.Book

@Controller
class AuthorController(
    private val authorRepo: AuthorRepository,
    private val bookRepository: BookRepository
) {
    @QueryMapping
    fun getAuthorById(@Argument id: Long) : AuthorEntity? = authorRepo.findById(id).orElse(null)
    @QueryMapping
    fun getAllAuthor() : MutableList<AuthorEntity> = authorRepo.findAll()
    @MutationMapping
    fun addAuthor(
        @Argument name: String,
        @Argument birthYear: Int?,
        @Argument nationality: String?,
        @Argument bookId: Long?
    ): AuthorEntity {
        val author = AuthorEntity(name = name, birthYear = birthYear, nationality = nationality, bookId = bookId)
        return authorRepo.save(author)
    }

    // Mutation: updateAuthor
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

    // Mutation: deleteAuthor
    @MutationMapping
    fun deleteAuthor(@Argument id: Long): Boolean {
        return if (authorRepo.existsById(id)) {
            authorRepo.deleteById(id)
            true
        } else false
    }

    // Mapping: lấy danh sách books của Author
    @SchemaMapping
    fun books(author: AuthorEntity): List<Book> {
        return bookRepository.findByAuthorId(author.id!!)
    }

}