package com.arfdevs.myproject.core.data.remote.datasource

import android.util.Log
import com.arfdevs.myproject.core.data.remote.ApiEndpoint
import com.arfdevs.myproject.core.data.remote.responses.PopularResponse
import com.arfdevs.myproject.core.helper.safeApiCall

class RemoteDataSource(private val endpoint: ApiEndpoint) {

    suspend fun fetchPopular(page: Int): PopularResponse {
        return safeApiCall {
            endpoint.fetchPopularMovies(page = page)
        }
    }

}