package com.example.quan_ly_sach.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface CommonRepository <E, ID> : JpaRepository<E, ID>   {
}