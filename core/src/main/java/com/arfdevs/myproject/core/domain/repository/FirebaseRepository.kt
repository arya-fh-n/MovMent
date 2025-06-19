package com.arfdevs.myproject.core.domain.repository

import android.os.Bundle
import com.arfdevs.myproject.core.domain.model.MovieTransactionModel
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel
import com.arfdevs.myproject.core.helper.DomainResult

interface FirebaseRepository {

    suspend fun getConfigTokenTopupList(): DomainResult<String>

    suspend fun updateConfigTokenTopupList(): DomainResult<Boolean>

    suspend fun getConfigPaymentMethodsList(): DomainResult<String>

    suspend fun updateConfigPaymentMethodsList(): DomainResult<Boolean>

    fun logEvent(eventName: String, bundle: Bundle)

    suspend fun insertTokenTransaction(
        transaction: TokenTransactionModel,
        userId: String
    ): DomainResult<Boolean>

    suspend fun getTokenBalance(userId: String): DomainResult<Int>

    suspend fun insertMovieTransaction(
        movieTransaction: MovieTransactionModel,
        userId: String
    ): DomainResult<Boolean>

    suspend fun getMovieTransaction(userId: String): DomainResult<List<MovieTransactionModel>>

}
