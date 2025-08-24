package com.example.quan_ly_sach.services.impl

import com.example.quan_ly_sach.repositories.CommonRepository
import com.example.quan_ly_sach.services.CommonService
import java.io.Serializable

open class CommonServiceImpl<E: Any, ID  : Serializable, R : CommonRepository<E, ID>>(protected val repo: R) :
    CommonService<E, ID> {
    override fun getAll(): List<E> = repo.findAll()

    override fun deleteAll() = repo.deleteAll()

    override fun exists(id: ID): Boolean = repo.existsById(id)

    override fun delete(id: ID) = repo.deleteById(id)

    override fun save(entityList: List<E>): List<E> = repo.saveAll(entityList)

    override fun save(entity: E): E = repo.save(entity)

    override fun getById(id: ID): E = repo.getById(id)
}