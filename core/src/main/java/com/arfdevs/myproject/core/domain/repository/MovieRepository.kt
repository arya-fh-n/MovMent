package com.arfdevs.myproject.core.domain.repository

import android.util.Log
import com.arfdevs.myproject.core.data.remote.datasource.RemoteDataSource
import com.arfdevs.myproject.core.data.remote.responses.PopularResponse
import com.arfdevs.myproject.core.helper.safeDataCall

interface MovieRepository {

    suspend fun fetchPopular(page: Int): PopularResponse

}

class MovieRepositoryImpl(
    private var remote: RemoteDataSource
): MovieRepository {

    override suspend fun fetchPopular(page: Int): PopularResponse = safeDataCall {
        val response = remote.fetchPopular(page)
        Log.d("Retrofit REPOSITORY", "fetchPopular: $response")
        response
    }

}