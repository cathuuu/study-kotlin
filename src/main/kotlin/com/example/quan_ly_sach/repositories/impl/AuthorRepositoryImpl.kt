package com.example.quan_ly_sach.repositories.impl

import com.example.quan_ly_sach.components.BaseNativeQuery
import com.example.quan_ly_sach.dtos.Queries.AuthorQueryDto
import com.example.quan_ly_sach.dtos.Queries.ConditionDto
import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.repositories.AuthorRepositoryCustom

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class AuthorRepositoryImpl(
    private val baseNativeQuery: BaseNativeQuery
) : AuthorRepositoryCustom {
    override fun findAllAuthorPagedNative(query: AuthorQueryDto, pageable: Pageable): Page<AuthorEntity> {
        val sqlBuilder = StringBuilder("SELECT a.id, a.name, a.birth_year, a.nationality FROM authors a WHERE 1=1")
        val countSqlBuilder = StringBuilder("SELECT COUNT(*) FROM authors a WHERE 1=1")

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
            AuthorEntity::class.java,
            conditionData.params
        )
    }

    private fun makeCondition(query: AuthorQueryDto): ConditionDto {
        val condition = StringBuilder()
        val params = mutableMapOf<String, Any>()

        query.name?.let {
            condition.append(" AND LOWER(a.name) LIKE LOWER(:name)")
            params["name"] = "%$it%"
        }
        query.birthYear?.let {
            condition.append(" AND a.birth_year = :birthYear")
            params["birthYear"] = it
        }
        query.nationality?.let {
            condition.append(" AND LOWER(a.nationality) LIKE LOWER(:nationality)")
            params["nationality"] = "%$it%"
        }

        return ConditionDto(
            queryCondition = condition.toString(),
            params = params
        )
    }
}