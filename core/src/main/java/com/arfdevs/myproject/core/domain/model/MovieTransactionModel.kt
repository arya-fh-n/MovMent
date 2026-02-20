package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MovieTransactionModel(

    var transactionId: String = "",

    var uid: String = "",

    var movies: List<CheckoutModel?> = listOf(),

    var total: Int = 0,

    var date: String = ""

) : Parcelable
