package com.example.quan_ly_sach.services

import com.example.quan_ly_sach.dtos.BorrowInputDto
import com.example.quan_ly_sach.entities.BorrowingEntity

interface BorrowingService : CommonService<BorrowingEntity, Long> {
    fun getBorrowBookByUserId(userId: Long): List<BorrowingEntity>
    fun createBorrow(input: BorrowInputDto, userId: Long, bookId: Long): BorrowingEntity
    fun updateBorrow(input: BorrowInputDto, userId: Long, bookId: Long, id: Long): BorrowingEntity
    fun deleteBorrow(userId: Long): Boolean
}