package com.example.quan_ly_sach.exceptions

import com.example.quan_ly_sach.models.ErrorModel
import lombok.Data

@Data
class AppException : RuntimeException {

    val errorModel: ErrorModel?

    constructor(message: String) : super(message) {
        this.errorModel = null
    }

    constructor(errorModel: ErrorModel) : super(errorModel.message) {
        this.errorModel = errorModel
    }

    companion object {
        fun of(errorModel: ErrorModel): AppException {
            return AppException(errorModel)
        }
    }
}