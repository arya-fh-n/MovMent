package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MovieDetailsModel(

    var id: Int = 0,

    var originalTitle: String = "",

    var overview: String = "",

    var backdropPath: String = "",

    var voteAverage: Double = 0.0,

    var price: Int = 0,

    var tagline: String = "",

    var genres: List<String> = listOf()

) : Parcelable
