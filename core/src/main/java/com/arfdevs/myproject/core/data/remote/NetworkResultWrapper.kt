package com.arfdevs.myproject.core.data.remote

import com.google.gson.JsonObject

sealed class NetworkResultWrapper<T> {
    data class Success<T>(val code: Int, val data: T, val headers: Map<String, List<String>>) :
        NetworkResultWrapper<T>()

    data class Error<T>(
        val code: Int,
        val data: JsonObject,
        val headers: Map<String, List<String>>
    ) : NetworkResultWrapper<T>() {
        val message
            get() = try {
                data.get("message")?.asString
            } catch (e: kotlin.Exception) {
                ""
            }
    }

    data class Exception<T>(val throwable: Throwable) : NetworkResultWrapper<T>()
}