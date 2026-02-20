package com.arfdevs.myproject.core.helper

sealed class UiState<out R> {
    data class Success<out R>(val data: R) : UiState<R>()
    data class Error<R>(
        val message: String = "",
        val errorCode: Int = 0,
        val data: R? = null
    ) : UiState<R>()
    object Loading : UiState<Nothing>()
    object Empty : UiState<Nothing>()
    object ErrorConnection : UiState<Nothing>()
}

fun <T> UiState<T>.isError(withEmpty: Boolean = true): Boolean =
    this is UiState.Error ||
            this is UiState.ErrorConnection ||
            (this is UiState.Empty && withEmpty)
