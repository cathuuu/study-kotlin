package com.example.quan_ly_sach.repositories

import com.example.quan_ly_sach.entities.BorrowingEntity
import com.example.quan_ly_sach.entities.UserEntity
import org.springframework.stereotype.Repository


@Repository
interface BorrowingRepository : CommonRepository<BorrowingEntity, Long>{
    fun findByUsers_Id(userId: Long): List<BorrowingEntity>
}