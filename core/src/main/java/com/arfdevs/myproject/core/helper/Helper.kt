package com.arfdevs.myproject.core.helper

import android.view.View
import androidx.core.view.isVisible

fun View.visible(state: Boolean) {
    this.isVisible = state
}

fun String?.posterPathNullHandling(): String {
    return if (this == null) {
        ""
    } else Constants.BACKDROP_PATH + this
}