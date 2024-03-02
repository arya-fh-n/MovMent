package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaymentMethodModel(

    var image: String = "",

    var label: String = "",

    var status: Boolean = false

) : Parcelable