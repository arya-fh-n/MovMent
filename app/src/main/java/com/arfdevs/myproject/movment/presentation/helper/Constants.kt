package com.arfdevs.myproject.movment.presentation.helper

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Constants {

    const val USERNAME = "Username"
    const val ERROR = "Error"

    const val INDONESIAN = "in"
    const val ENGLISH = "en"

    const val VIEW_ALPHA = 0.4f

    fun getCurrentDateInDDMMYYYYFormat(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    fun String.convertTotransactionID(): String {
        val parts = this.split(" ")[0].split("-") + this.split(" ")[1].split(":")
        return "TR-${parts[0]}${parts[1]}${parts[2]}${parts[3]}${parts[4]}${parts[5]}"
    }

    fun String.convertToDateInDDMMYYYYFormat(): String {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(this)
        return outputFormat.format(date)
    }
}