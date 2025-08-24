package com.example.quan_ly_sach.repositories

import com.example.quan_ly_sach.entities.AuthorEntity
import org.springframework.stereotype.Repository
import org.yaml.snakeyaml.events.Event

@Repository
interface AuthorRepository : CommonRepository<AuthorEntity, Long> {
}