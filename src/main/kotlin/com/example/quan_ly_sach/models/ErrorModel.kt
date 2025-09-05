package com.example.quan_ly_sach.models

import java.io.Serializable

data class ErrorModel(
    val statusCode: Long?,
    val message: String
) : Serializable {

    companion object {
        fun of(statusCode: Long, message: String): ErrorModel {
            return ErrorModel(statusCode, message)
        }

        fun of(statusCode: Long): ErrorModel {
            return ErrorModel(statusCode, "")
        }

        fun of(message: String): ErrorModel {
            return ErrorModel(null, message)
        }
    }
}