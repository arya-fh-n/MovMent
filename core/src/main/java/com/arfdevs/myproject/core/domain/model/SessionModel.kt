package com.arfdevs.myproject.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SessionModel(

    val displayName: String = "",

    val uid: String = "",

    val onboardingState: Boolean = false

): Parcelable