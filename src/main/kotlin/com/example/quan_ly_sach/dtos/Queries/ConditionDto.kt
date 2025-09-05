package com.example.quan_ly_sach.dtos.Queries

data class ConditionDto(
    val params: Map<String, Any> = emptyMap(),
    val queryCondition: String = ""
) {
}