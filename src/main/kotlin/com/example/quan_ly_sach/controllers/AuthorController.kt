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
import com.example.quan_ly_sach.services.AuthorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class AuthorController(
    private val bookRepo: BookRepository,
    private val authorService: AuthorService
) {
    // ================= QUERIES =================
    @QueryMapping
    fun getAuthorById(@Argument id: Long): AuthorEntity? =
        authorService.getAuthorById(id)

    @QueryMapping
    fun getAuthorsByPage(
        @Argument page: Int = 0,
        @Argument size: Int = 10
    ): AuthorPage {
        return authorService.getAuthorsByPage(page, size)
    }


    @QueryMapping
    fun getAllAuthors(): List<AuthorEntity> = authorService.getAllAuthors()

    // ================= MUTATIONS =================
    @MutationMapping
    fun addAuthor(@Argument input: AuthorInputDto): AuthorEntity {
        return authorService.addAuthor(input)
    }

    @MutationMapping
    fun updateAuthor(
        @Argument id: Long,
        @Argument input: AuthorInputDto
    ): AuthorEntity {
         return authorService.updateAuthor(id, input)
    }

    @MutationMapping
    fun deleteAuthor(@Argument id: Long): Boolean {
       return authorService.deleteAuthor(id)
    }

    @QueryMapping
    fun searchAuthors(@Argument filter: AuthorSearchInputDto?): List<AuthorEntity> {
        return authorService.searchAuthors(filter)
    }
    // ================= RELATION =================
    @SchemaMapping(typeName = "Author", field = "books")
    fun books(author: AuthorEntity): List<BookEntity> {
        return bookRepo.findByAuthorId(author.id!!)
    }
}
