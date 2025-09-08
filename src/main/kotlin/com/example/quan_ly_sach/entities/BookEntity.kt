package com.example.quan_ly_sach.entities

import jakarta.persistence.*

@Entity
@Table(name = "books")
data class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null,

    @Column(nullable = false, columnDefinition = "text")
    var title: String?,

    var publishedYear: Int? = null,

    var price: Double? = null,

    var quantity: Int? = null,

    @ManyToOne
    @JoinColumn(name = "author_id")
    var author: AuthorEntity? = null
)