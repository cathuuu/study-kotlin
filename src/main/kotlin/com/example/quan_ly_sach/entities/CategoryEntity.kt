package com.example.quan_ly_sach.entities

import jakarta.persistence.*

@Entity
@Table(name = "categories")
data class CategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long,
    var name: String
)
