package com.example.quan_ly_sach.services.impl

import com.example.quan_ly_sach.entities.BookEntity
import com.example.quan_ly_sach.entities.UserEntity
import com.example.quan_ly_sach.repositories.BookRepository
import com.example.quan_ly_sach.repositories.UserRepository
import com.example.quan_ly_sach.services.BookService
import com.example.quan_ly_sach.services.UserService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl
    (repo: UserRepository) : CommonServiceImpl<UserEntity, Long, UserRepository>(repo),UserService{
    override fun findByUsername(username: String): UserEntity {
        return repo.findByUsername(username)
    }
}