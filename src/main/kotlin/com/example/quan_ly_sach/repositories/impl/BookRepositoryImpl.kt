package com.example.quan_ly_sach.repositories.impl

import com.example.quan_ly_sach.components.BaseNativeQuery
import com.example.quan_ly_sach.dtos.Queries.AuthorQueryDto
import com.example.quan_ly_sach.dtos.Queries.BookQueryDto
import com.example.quan_ly_sach.dtos.Queries.ConditionDto
import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.entities.BookEntity
import com.example.quan_ly_sach.repositories.BookRepositoryCustom
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository


@Repository
class BookRepositoryImpl(
    private val baseNativeQuery : BaseNativeQuery
) :BookRepositoryCustom {
    override fun findAllBooksPagedNavtive(query: BookQueryDto, pageable: Pageable): Page<BookEntity> {
        val sqlBuilder = StringBuilder(
            "SELECT b.id, b.title, b.published_year, b.price, b.quantity, b.author_id FROM book b WHERE 1=1"
        )
        val countSqlBuilder = StringBuilder("SELECT COUNT(*) FROM books b WHERE 1=1")

        val conditionData = makeCondition(query)

        if (conditionData.queryCondition.isNotBlank()) {
            sqlBuilder.append(conditionData.queryCondition)
            countSqlBuilder.append(conditionData.queryCondition)
        }

        if (!query.sortBy.isNullOrBlank() && !query.sortDir.isNullOrBlank()) {
            sqlBuilder.append(" ORDER BY ${query.sortBy} ${query.sortDir}")
        }

        return baseNativeQuery.findPage(
            sqlBuilder.toString(),
            countSqlBuilder.toString(),
            pageable,
            BookEntity::class.java,
            conditionData.params
        )
    }

    private fun makeCondition(bookQueryDto: BookQueryDto): ConditionDto {
        val condition = StringBuilder()
        val params = mutableMapOf<String, Any>()

        val id = bookQueryDto.id
        val title = bookQueryDto.title
        val publishedYear = bookQueryDto.publishedYear
        val price = bookQueryDto.price
        val quantity = bookQueryDto.quantity
        val author = bookQueryDto.author

        if (id != null) {
            condition.append(" AND b.id = :id")
            params["id"] = id
        }
        if (!title.isNullOrBlank()) {
            condition.append(" AND LOWER(b.title) LIKE LOWER(:title)")
            params["title"] = "%$title%"
        }
        if (publishedYear != null) {
            condition.append(" AND b.published_year = :publishedYear")
            params["publishedYear"] = publishedYear
        }
        if (price != null) {
            condition.append(" AND b.price = :price")
            params["price"] = price
        }
        if (quantity != null) {
            condition.append(" AND b.quantity = :quantity")
            params["quantity"] = quantity
        }
        if (author != null) {
            condition.append(" AND b.author_id = :authorId")
            params["authorId"] = author.id!!
        }

        return ConditionDto(
            queryCondition = condition.toString(),
            params = params
        )
    }

}