package com.arfdevs.myproject.core.helper

sealed class UiState<out R> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val error: Throwable) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}

fun <T> UiState<T>.onSuccess(
    execute: (data: T) -> Unit
): UiState<T> = apply {
    if (this is UiState.Success) {
        execute(data)
    }
}

fun <T> UiState<T>.onError(
    execute: (error: Throwable) -> Unit
): UiState<T> = apply {
    if (this is UiState.Error) {
        execute(error)
    }
}

fun <T> UiState<T>.onLoading(
    execute: () -> Unit
): UiState<T> = apply {
    if (this is UiState.Loading) {
        execute()
    }
}
