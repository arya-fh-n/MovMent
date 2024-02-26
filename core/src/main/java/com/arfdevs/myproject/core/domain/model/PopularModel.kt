package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularModel(
    var originalTitle: String = "",
    var posterPath: String = "",
    var voteAverage: Double = 0.0
): Parcelable