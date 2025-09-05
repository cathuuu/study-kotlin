package com.example.quan_ly_sach.repositories

import com.example.quan_ly_sach.entities.AuthorEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.yaml.snakeyaml.events.Event

@Repository
interface AuthorRepository : CommonRepository<AuthorEntity, Long>, AuthorRepositoryCustom {
    @Query("""
    SELECT a FROM AuthorEntity a
    WHERE (:keyword IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:nationality IS NULL OR (a.nationality IS NOT NULL AND LOWER(a.nationality) = LOWER(CAST(:nationality AS string))))
      AND (:birthYear IS NULL OR a.birthYear = :birthYear)
""")
    fun searchAuthors(
        @Param("keyword") keyword: String?,
        @Param("nationality") nationality: String?,
        @Param("birthYear") birthYear: Int?
    ): List<AuthorEntity>
}