package com.arfdevs.myproject.core.domain.repository

import android.os.Bundle
import com.arfdevs.myproject.core.domain.model.MovieTransactionModel
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel

interface FirebaseRepository {

    fun getConfigTokenTopupList(): String

    fun updateConfigTokenTopupList(): Boolean

    fun getConfigPaymentMethodsList(): String

    fun updateConfigPaymentMethodsList(): Boolean

    fun logEvent(eventName: String, bundle: Bundle)

    fun insertTokenTransaction(
        transaction: TokenTransactionModel,
        userId: String
    ): Boolean

    fun getTokenBalance(userId: String): Int

    fun insertMovieTransaction(
        movieTransaction: MovieTransactionModel,
        userId: String
    ): Boolean

    fun getMovieTransaction(userId: String): List<MovieTransactionModel>

}
