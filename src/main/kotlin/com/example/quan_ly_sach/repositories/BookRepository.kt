package com.example.quan_ly_sach.repositories

import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.entities.BookEntity
import org.springframework.data.domain.Page

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.awt.print.Book


@Repository
interface BookRepository: CommonRepository<BookEntity, Long>, BookRepositoryCustom {
    fun findByAuthorId(authorId: Long): List<BookEntity>
    fun findByAuthorName(authorName: String): List<BookEntity>
    @Query(
        """
    SELECT b FROM BookEntity b 
    WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(:title))
      AND (:publishedYear IS NULL OR b.publishedYear = :publishedYear)
      AND (:minPrice IS NULL OR b.price >= :minPrice)
      AND (:maxPrice IS NULL OR b.price <= :maxPrice)
      AND (:minQuantity IS NULL OR b.quantity >= :minQuantity)
      AND (:maxQuantity IS NULL OR b.quantity <= :maxQuantity)
    """
    )
    fun searchBooks(
        @Param("title") title: String?,
        @Param("publishedYear") publishedYear: Int?,
        @Param("minPrice") minPrice: Double?,
        @Param("maxPrice") maxPrice: Double?,
        @Param("minQuantity") minQuantity: Int?,
        @Param("maxQuantity") maxQuantity: Int?
    ): List<BookEntity>

}