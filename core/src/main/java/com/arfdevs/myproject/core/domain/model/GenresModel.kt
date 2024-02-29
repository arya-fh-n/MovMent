package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class GenresModel(
    var id: Int = 0,
    var name: String = ""
) : Parcelable