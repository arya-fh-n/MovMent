package com.arfdevs.myproject.core.data.remote

import com.arfdevs.myproject.core.data.remote.responses.NowPlayingResponse
import com.arfdevs.myproject.core.data.remote.responses.PopularResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoint {

    @GET("movie/popular")
    suspend fun fetchPopularMovies(
        @Query("page") page: Int? = null,
        @Query("region") region: String? = "ID"
    ): PopularResponse

    @GET("movie/now_playing")
    suspend fun fetchNowPlayingMovies(
        @Query("page") page: Int? = null,
        @Query("region") region: String? = "ID"
    ): NowPlayingResponse

}