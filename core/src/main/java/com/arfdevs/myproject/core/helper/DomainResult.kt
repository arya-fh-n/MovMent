package com.arfdevs.myproject.core.helper

sealed class DomainResult<out Result> {

    data class Success<Result>(val data: Result): DomainResult<Result>()
    data class TechnicalError(val code: Int): DomainResult<Nothing>()
    data class EmptyState(
        val message: String?,
        val responseStatusCode: Int? = 0,
        val statusCode: Int? = 0
    ): DomainResult<Nothing>()
    data class ErrorState<Result>(
        val message: String? = "",
        val responseStatusCode: Int? = 0,
        val data: Result? = null
    ) : DomainResult<Result>()
    data object NetworkError : DomainResult<Nothing>()
}
