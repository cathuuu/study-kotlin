package com.example.quan_ly_sach.components

import com.example.quan_ly_sach.exceptions.AppException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.util.HashMap

@Component
class BaseNativeQuery {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    // --------- Using JdbcTemplate (positional parameters) ---------

    fun <T> findPage(sql: String, countSql: String, pageable: Pageable, type: Class<T>, vararg params: Any): Page<T> {
        val paginatedSql = "$sql LIMIT ? OFFSET ?"
        val paginatedParams = arrayOf(*params, pageable.pageSize.toLong(), pageable.offset.toLong())

        val content = jdbcTemplate.query(paginatedSql, BeanPropertyRowMapper(type), *paginatedParams)
        val total = jdbcTemplate.queryForObject(countSql, Long::class.java, *params) ?: 0L

        return PageImpl(content, pageable, total)
    }

    fun <T> findList(sql: String, type: Class<T>, vararg params: Any): List<T> {
        return jdbcTemplate.query(sql, BeanPropertyRowMapper(type), *params)
    }

    fun <T> findOne(sql: String, type: Class<T>, vararg params: Any): T? {
        val results = jdbcTemplate.query(sql, BeanPropertyRowMapper(type), *params)
        return results.firstOrNull()
    }

    // --------- Using NamedParameterJdbcTemplate (named parameters) ---------

    fun <T> findPage(sql: String, countSql: String, pageable: Pageable, type: Class<T>, params: Map<String, Any>): Page<T> {
        val paginatedSql = "$sql LIMIT :limit OFFSET :offset"
        val pageCurrent = pageable.pageNumber.toLong()
        val pageSize = pageable.pageSize.toLong()

        if (pageCurrent < 0) {
            throw AppException("Page number must not be negative")
        }

        val paginatedParams = HashMap(params)
        paginatedParams["limit"] = pageSize
        paginatedParams["offset"] = pageable.offset

        val content = namedParameterJdbcTemplate.query(paginatedSql, paginatedParams, BeanPropertyRowMapper(type))
        val total = namedParameterJdbcTemplate.queryForObject(countSql, params, Long::class.java) ?: 0L

        return PageImpl(content, pageable, total)
    }

    fun <T> findList(sql: String, type: Class<T>, params: Map<String, Any>): List<T> {
        return namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper(type))
    }

    fun <T> findOne(sql: String, type: Class<T>, params: Map<String, Any>): T? {
        val results = namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper(type))
        return results.firstOrNull()
    }
}