package com.example.quan_ly_sach.services.impl

import com.example.quan_ly_sach.dtos.AuthorInputDto
import com.example.quan_ly_sach.dtos.AuthorPage
import com.example.quan_ly_sach.dtos.AuthorSearchInputDto
import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.repositories.AuthorRepository
import com.example.quan_ly_sach.repositories.BookRepository
import com.example.quan_ly_sach.services.AuthorService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl
    (repo: AuthorRepository,

) : CommonServiceImpl<AuthorEntity, Long, AuthorRepository>(repo), AuthorService    {
    override fun getAuthorById(id: Long): AuthorEntity? {
        return repo.findById(id).orElse(null)
    }

    override fun getAllAuthors(): List<AuthorEntity> {
        return repo.findAll()
    }

    override fun getAuthorsByPage(page: Int, size: Int): AuthorPage {
        val pageable = PageRequest.of(page, size)
        val result = repo.findAll(pageable)
        return AuthorPage(
            content = result.content,
            totalElements = result.totalElements,
            totalPages = result.totalPages,
            number = result.number,
            size = result.size
        )
    }

    override fun addAuthor(input: AuthorInputDto): AuthorEntity {
        val author = AuthorEntity(
            name = input.name,
            birthYear = input.birthYear,
            nationality = input.nationality
        )
        return repo.save(author)
    }

    override fun updateAuthor(id: Long, input: AuthorInputDto): AuthorEntity {
        val author = repo.findById(id).orElseThrow {
            NoSuchElementException("Author with ID $id not found.")
        }

        // Cập nhật các field từ input
        author.name = input.name
        author.birthYear = input.birthYear
        author.nationality = input.nationality

        return repo.save(author)
    }

    override fun deleteAuthor(id: Long): Boolean {
        return if (repo.existsById(id)) {
            repo.deleteById(id)
            true
        } else false
    }

    override fun searchAuthors(filter: AuthorSearchInputDto?): List<AuthorEntity> {
        return repo.searchAuthors(
            keyword = filter?.keyword,
            nationality = filter?.nationality,
            birthYear = filter?.birthYear
        )
    }
}