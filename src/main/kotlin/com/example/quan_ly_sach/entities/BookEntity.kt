package com.example.quan_ly_sach.entities

import jakarta.persistence.*

@Entity
@Table(name = "books")
data class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var title: String,   // ✅ Bắt buộc, không nullable

    var publishedYear: Int? = null,
    var price: Double? = null,
    var quantity: Int? = null,

    @ManyToOne
    @JoinColumn(name = "author_id")
    var author: AuthorEntity? = null,

    @ManyToOne   // ✅ đổi OneToOne → ManyToOne
    @JoinColumn(name = "publisher_id")
    var publisher: PublisherEntity? = null   // ✅ đổi tên field, class chuẩn
)
