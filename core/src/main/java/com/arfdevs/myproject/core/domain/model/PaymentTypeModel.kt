package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaymentTypeModel(

    var item: List<PaymentMethodModel> = listOf(),

    val title: String = ""

): Parcelable