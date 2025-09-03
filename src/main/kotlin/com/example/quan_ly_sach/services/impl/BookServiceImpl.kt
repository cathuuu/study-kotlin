package com.example.quan_ly_sach.services.impl

import com.example.quan_ly_sach.entities.BookEntity
import com.example.quan_ly_sach.repositories.AuthorRepository
import com.example.quan_ly_sach.repositories.BookRepository
import com.example.quan_ly_sach.services.AuthorService
import com.example.quan_ly_sach.services.BookService
import com.example.quan_ly_sach.services.CommonService
import org.springframework.stereotype.Service

@Service
class   BookServiceImpl
    (repo: BookRepository) : CommonServiceImpl<BookEntity, Long, BookRepository>(repo), BookService {
}