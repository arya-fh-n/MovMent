package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TokenTransactionModel(

    var uid: String = "",

    var price: Int = 0,

    var token: Int = 0,

    var method: String = "",

    var date: String = ""
): Parcelable