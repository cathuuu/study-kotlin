package com.example.quan_ly_sach.entities

import jakarta.persistence.*

@Entity
@Table(name = "publishers")
data class PublisherEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var country: String
)
