package com.example.quan_ly_sach.mappers

import com.example.quan_ly_sach.dtos.BorrowInputDto
import com.example.quan_ly_sach.entities.BorrowingEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BorrowingMapper {
    fun toDto(entity: BorrowingEntity): BorrowInputDto
    fun toEntity(dto: BorrowInputDto): BorrowingEntity
}
