package com.example.quan_ly_sach.repositories

import com.example.quan_ly_sach.entities.BookEntity
import org.springframework.stereotype.Repository
import java.awt.print.Book

@Repository
interface BookRepository: CommonRepository<BookEntity, Long> {
    fun findByAuthorId(authorId: Long): List<BookEntity>
    fun findByAuthorName(authorName: String): List<BookEntity>
}