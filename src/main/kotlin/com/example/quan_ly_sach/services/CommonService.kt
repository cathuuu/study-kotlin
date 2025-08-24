package com.example.quan_ly_sach.services

import java.io.Serializable

interface CommonService<E: Any, ID : Serializable>  {
    fun getAll(): List<E>
    fun getById(id: ID): E
    fun save(entity: E): E
    fun save(entityList: List<E>): List<E>
    fun delete(id: ID)
    fun deleteAll()
    fun exists(id: ID): Boolean
}