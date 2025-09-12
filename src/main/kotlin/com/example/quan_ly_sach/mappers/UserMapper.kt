package com.example.quan_ly_sach.mappers

import com.example.quan_ly_sach.dtos.AuthenticationDto.RegisterInput
import com.example.quan_ly_sach.dtos.UserDto
import com.example.quan_ly_sach.entities.UserEntity
import org.mapstruct.Mapper

@Mapper
interface UserMapper {
    fun toDto(entity: UserEntity): UserDto
    fun toEntity(dto: UserDto): UserEntity

    fun fromRegisterInput(input: RegisterInput): UserEntity
}