package com.example.quan_ly_sach.entities

import jakarta.persistence.*
import java.awt.print.Book

@Entity
@Table(name = "authors")
data class AuthorEntity(
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
var id: Long? = null,

@Column(nullable = false)
var name: String,

var birthYear: Int? = null,

var nationality: String? = null,

var bookId: Long? = null
)

