package com.bangkit.emergenz.util

sealed class ApiResult<out T> {
        data class Error(val errorMessage: String) : ApiResult<Nothing>()
    }