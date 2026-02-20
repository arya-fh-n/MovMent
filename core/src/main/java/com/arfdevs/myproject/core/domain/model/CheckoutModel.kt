package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CheckoutModel(
    var movieId: Int = 0,

    var userId: String = "",

    var originalTitle: String = "",

    var posterPath: String? = "",

    var voteAverage: Double = 0.0,

    var price: Int = 0,
) : Parcelable
