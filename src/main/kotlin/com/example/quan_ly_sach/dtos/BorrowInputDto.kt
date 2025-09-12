package com.example.quan_ly_sach.dtos

import com.example.quan_ly_sach.enums.Status
import java.time.LocalDate
import java.util.*

data class BorrowInputDto(
    var borrowDate: LocalDate,
    var returnDate: LocalDate,
    var status: Status
)
