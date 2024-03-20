package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WishlistModel(

    var movieId: Int = 0,

    var userId: String = "",

    var originalTitle: String = "",

    var posterPath: String? = "",

    var voteAverage: Double = 0.0,

    var price: Int = 0,

    var isInCart: Boolean = false

) : Parcelable
