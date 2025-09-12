package com.example.quan_ly_sach.services.impl

import com.example.quan_ly_sach.dtos.BookInputDto
import com.example.quan_ly_sach.dtos.BookPage
import com.example.quan_ly_sach.dtos.BookSearchInputDto
import com.example.quan_ly_sach.entities.BookEntity
import com.example.quan_ly_sach.repositories.AuthorRepository
import com.example.quan_ly_sach.repositories.BookRepository
import com.example.quan_ly_sach.services.AuthorService
import com.example.quan_ly_sach.services.BookService
import com.example.quan_ly_sach.services.CommonService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class BookServiceImpl
    (
    repo: BookRepository,
    private val authorRepo: AuthorRepository) : CommonServiceImpl<BookEntity, Long, BookRepository>(repo), BookService {
    override fun getBookById(id: Long): BookEntity {
       return repo.findById(id).orElse(null)
    }

    override fun getAllBooks(): List<BookEntity> {
        return repo.findAll()
    }

    override fun addBook(authorId: Long, input: BookInputDto): BookEntity {
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
        return repo.save(book)
    }

    override fun updateBook(id: Long, authorId: Long?, input: BookInputDto): BookEntity {
        val book = repo.findById(id).orElseThrow {
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

        return repo.save(book)
    }

    override fun deleteBook(id: Long): Boolean {
        return if (repo.existsById(id)) {
            repo.deleteById(id)
            true
        } else false
    }

    override fun searchBooks(filter: BookSearchInputDto?): List<BookEntity> {
        return repo.searchBooks(
            title = "%${filter?.title?:""}%",
            publishedYear = filter?.publishedYear,
            minPrice = filter?.minPrice,
            maxPrice = filter?.maxPrice,
            minQuantity = filter?.minQuantity,
            maxQuantity = filter?.maxQuantity
        )
    }

    override fun getBooksByPage(page: Int, size: Int): BookPage {
        val pageable = PageRequest.of(page, size)
        val result = repo.findAll(pageable)
        return BookPage(
            content = result.content,
            totalElements = result.totalElements,
            totalPages = result.totalPages,
            number = result.number,
            size = result.size
        )
    }
}