package com.arfdevs.myproject.core.helper

sealed class SourceResult<T> {
    data class Success<T>(val data: T): SourceResult<T>()
    data class Error<T>( val throwable: Throwable): SourceResult<T>()
}