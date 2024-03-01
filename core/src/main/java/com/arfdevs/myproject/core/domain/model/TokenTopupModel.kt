package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TokenTopupModel(

    var price: Int = 0,

    var token: Int = 0,

): Parcelable