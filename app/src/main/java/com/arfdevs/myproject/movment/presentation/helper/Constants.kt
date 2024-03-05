package com.arfdevs.myproject.movment.presentation.helper

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Constants {

    const val SPLASH_DELAY = 3000L
    const val HOME_TOKEN_FETCH_DELAY = 5L
    const val TOKEN_PRICE_PER_RUPIAH = 150

    const val USERNAME_MIN_LENGTH = 3
    const val PASSWORD_MAX_LENGTH = 8

    const val USERNAME = "Username"
    const val ERROR = "Error"

    const val INDONESIAN = "in"
    const val ENGLISH = "en"

    const val VIEW_ALPHA = 0.4f

    const val VIBRATE_1000 = 1000L
    const val VIBRATE_2000 = 2000L

    const val DELAY_500 = 500L

    const val NOTIFICATION_CHANNEL_ID = "notify-channel"

    const val TRANSACTION_ID = "transaction_id"
    const val TRANSACTION_MOVIE_COUNT = "transaction_movie_count"

    const val NOTIF_UNIQUE_WORK: String = "NOTIF_UNIQUE_WORK"

    fun getCurrentDateInDDMMYYYYFormat(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    fun String.convertTotransactionID(): String {
        val stringPart = this.split(" ")[0].split("-") + this.split(" ")[1].split(":")
        return "TR-${stringPart[0]}${stringPart[1]}${stringPart[2]}${stringPart[3]}${stringPart[4]}${stringPart[5]}"
    }

    fun String.convertToDateInDDMMYYYYFormat(): String {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(this)
        return outputFormat.format(date)
    }

}
