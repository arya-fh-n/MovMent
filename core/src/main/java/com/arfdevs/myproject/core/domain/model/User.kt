package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class User(
    var displayName: String = "",
    var email: String = "",
    var password: String = ""
) : Parcelable