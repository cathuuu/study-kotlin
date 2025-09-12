package com.example.quan_ly_sach.services.impl

import com.example.quan_ly_sach.dtos.BorrowInputDto
import com.example.quan_ly_sach.entities.BorrowingEntity
import com.example.quan_ly_sach.enums.Status
import com.example.quan_ly_sach.mappers.BorrowingMapper
import com.example.quan_ly_sach.repositories.BookRepository
import com.example.quan_ly_sach.repositories.BorrowingRepository
import com.example.quan_ly_sach.repositories.UserRepository
import com.example.quan_ly_sach.services.BorrowingService
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestParam

@Service
class BorrowingServiceImpl (
    repo: BorrowingRepository,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
    private val borrowingMapper: BorrowingMapper
) : CommonServiceImpl<BorrowingEntity, Long, BorrowingRepository>(repo),
    BorrowingService {
        @Transactional(readOnly = true)
    override fun getBorrowBookByUserId( userId: Long): List<BorrowingEntity> {
        return repo.findByUsers_Id(userId)
    }
    @Transactional
    override fun createBorrow(input: BorrowInputDto, userId: Long, bookId: Long): BorrowingEntity {
        var user = userRepository.findById(userId).orElseThrow {
            NoSuchElementException("No user with id $userId")
        }
        var book = bookRepository.findById(bookId).orElseThrow {
            NoSuchElementException("No book with id $bookId")
        }
        val borrow = borrowingMapper.toEntity(input).apply {
            this.users = user
            this.books = book
            this.status = Status.BORROWED
        }
        return repo.save(borrow)
    }
    @Transactional
    override fun updateBorrow(input: BorrowInputDto, userId: Long, bookId: Long, id: Long): BorrowingEntity {
        val borrow = repo.findById(id).orElseThrow {
            NoSuchElementException("No borrow with id $id")
        }

        val user = userRepository.findById(userId).orElseThrow {
            NoSuchElementException("No user with id $userId")
        }

        val book = bookRepository.findById(bookId).orElseThrow {
            NoSuchElementException("No book with id $bookId")
        }

        borrow.apply {
            this.users = user
            this.books = book
            this.borrowDate = input.borrowDate
            this.returnDate = input.returnDate
            this.status = input.status
        }

        return repo.save(borrow)
    }
    @Transactional
    override fun deleteBorrow(userId: Long): Boolean {
        return if (repo.existsById(userId))
        {
            repo.deleteById(userId)
            true
        }
        else false
    }
}