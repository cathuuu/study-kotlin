package com.example.quan_ly_sach.entities

import com.example.quan_ly_sach.enums.Status
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "borrowings")
data class BorrowingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var users: UserEntity? = null,
    @ManyToOne
    @JoinColumn(name = "book_id")
    var books: BookEntity? = null,
    var borrowDate: LocalDate = LocalDate.now(),
    var returnDate: LocalDate,
    @Enumerated(EnumType.STRING)
    var status: Status = Status.BORROWED

    )

