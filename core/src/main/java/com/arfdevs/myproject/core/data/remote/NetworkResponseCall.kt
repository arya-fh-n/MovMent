package com.arfdevs.myproject.core.data.remote

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class NetworkResponseCall<T>(
    private val delegate: Call<T>
) : Call<NetworkResultWrapper<T?>> {
    override fun clone(): Call<NetworkResultWrapper<T?>> = NetworkResponseCall(delegate.clone())

    override fun execute(): Response<NetworkResultWrapper<T?>> =
        throw UnsupportedOperationException("Does not support execute command")

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    override fun enqueue(callback: Callback<NetworkResultWrapper<T?>>) =
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                when (response.code()) {
                    in 200..208 -> {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(
                                NetworkResultWrapper.Success(
                                    code = response.code(),
                                    data = response.body(),
                                    headers = response.headers().toMultimap()
                                ),
                            ),
                        )
                    }

                    else -> {
                        val jsonObject = parseJson(response)
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(
                                NetworkResultWrapper.Error(
                                    code = response.code(),
                                    data = jsonObject,
                                    headers = response.headers().toMultimap()
                                ),
                            ),
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(NetworkResultWrapper.Exception(t)),
                )
                call.cancel()
            }

        })

    internal fun parseJson(response: Response<T>): JsonObject {
        return try {
            JsonParser.parseString(
                response.errorBody()?.toString()
            ).asJsonObject
        } catch (e: Exception) {
            JsonObject()
        }
    }
}