package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SearchModel(
    var id: Int = 0,

    var originalTitle: String = "",

    var posterPath: String = "",

    var voteAverage: Double = 0.0,

    var price: Int = 0

): Parcelable