package com.example.quan_ly_sach.services.impl

import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.repositories.AuthorRepository
import com.example.quan_ly_sach.services.AuthorService
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl
    (repo: AuthorRepository ) : CommonServiceImpl<AuthorEntity, Long, AuthorRepository>(repo), AuthorService    {
}