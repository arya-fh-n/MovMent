package com.arfdevs.myproject.core.helper

import android.view.View
import androidx.core.view.isVisible

fun View.visible(state: Boolean) {
    this.isVisible = state
}

fun View.visibleIf(state: Boolean) {
    if (state) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.enabled(state: Boolean) {
    this.isEnabled = state
}

fun String?.posterPathNullHandling(): String {
    return if (this == null) {
        ""
    } else Constants.BACKDROP_PATH + this
}

fun Int?.orZero(): Int = this ?: 0
