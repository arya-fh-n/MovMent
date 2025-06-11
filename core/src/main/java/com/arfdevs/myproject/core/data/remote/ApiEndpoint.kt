package com.arfdevs.myproject.core.data.remote

import com.arfdevs.myproject.core.data.remote.responses.MovieDetailsResponse
import com.arfdevs.myproject.core.data.remote.responses.MovieSearchResponse
import com.arfdevs.myproject.core.data.remote.responses.NowPlayingResponse
import com.arfdevs.myproject.core.data.remote.responses.PopularResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoint {

    @GET("movie/popular")
    suspend fun fetchPopularMovies(
        @Query("page") page: Int? = null,
        @Query("region") region: String? = "ID"
    ): NetworkResultWrapper<PopularResponse>

    @GET("movie/now_playing")
    suspend fun fetchNowPlayingMovies(
        @Query("page") page: Int? = null,
        @Query("region") region: String? = "ID"
    ): NetworkResultWrapper<NowPlayingResponse>

    @GET("movie/{movie_id}")
    suspend fun fetchMovieDetails(
        @Path("movie_id") movieId: Int
    ): NetworkResultWrapper<MovieDetailsResponse>

    @GET("search/movie")
    suspend fun fetchSearch(
        @Query("query") query: String = "",
        @Query("include_adult") includeAdult: Boolean? = false,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = "ID"
    ): NetworkResultWrapper<MovieSearchResponse>

}