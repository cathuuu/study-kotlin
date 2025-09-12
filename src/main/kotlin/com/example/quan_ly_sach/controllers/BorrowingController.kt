package com.example.quan_ly_sach.controllers

import com.example.quan_ly_sach.dtos.BorrowInputDto
import com.example.quan_ly_sach.entities.AuthorEntity
import com.example.quan_ly_sach.entities.BookEntity
import com.example.quan_ly_sach.entities.BorrowingEntity
import com.example.quan_ly_sach.entities.UserEntity
import com.example.quan_ly_sach.enums.Status
import com.example.quan_ly_sach.exceptions.AppException
import com.example.quan_ly_sach.repositories.AuthorRepository
import com.example.quan_ly_sach.repositories.BookRepository
import com.example.quan_ly_sach.repositories.BorrowingRepository
import com.example.quan_ly_sach.repositories.UserRepository
import com.example.quan_ly_sach.services.BookService
import com.example.quan_ly_sach.services.BorrowingService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@Controller
class BorrowingController
    (
     private val borrowingService: BorrowingService) {

    @QueryMapping
    fun getAllBorrow(): List<BorrowingEntity> = borrowingService.getAll()


    @QueryMapping
    fun getBorrowBookByUserId(@Argument userId: Long): List<BorrowingEntity> = borrowingService.getBorrowBookByUserId(userId)

    @MutationMapping
    fun createBorrow(
        @Argument input: BorrowInputDto,
        @Argument userId: Long,
        @Argument bookId: Long
    ): BorrowingEntity = borrowingService.createBorrow(input, userId, bookId)

    @MutationMapping
    fun updateBorrow(
        @Argument input: BorrowInputDto,
        @Argument userId: Long,
        @Argument bookId: Long,
        @Argument id: Long
    ): BorrowingEntity = borrowingService.updateBorrow(input, userId, bookId, id)
    @MutationMapping
    fun deleteBorrow(@Argument id: Long): Boolean = borrowingService.deleteBorrow(id)
    @SchemaMapping(typeName = "Borrowing", field = "book")
    fun book(borrowing: BorrowingEntity): BookEntity? {
        return borrowing.books
    }

    @SchemaMapping(typeName = "Borrowing", field = "user")
    fun user(borrowing: BorrowingEntity): UserEntity? {
        return borrowing.users
    }
}
