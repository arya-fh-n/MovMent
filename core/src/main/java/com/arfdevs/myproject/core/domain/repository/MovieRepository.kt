package com.arfdevs.myproject.core.domain.repository

import com.arfdevs.myproject.core.data.remote.datasource.RemoteDataSource
import com.arfdevs.myproject.core.data.remote.responses.MovieDetailsResponse
import com.arfdevs.myproject.core.data.remote.responses.NowPlayingResponse
import com.arfdevs.myproject.core.data.remote.responses.PopularResponse
import com.arfdevs.myproject.core.helper.safeDataCall

interface MovieRepository {

    suspend fun fetchPopular(page: Int): PopularResponse

    suspend fun fetchNowPlaying(page: Int): NowPlayingResponse

    suspend fun fetchMovieDetails(movieId: Int): MovieDetailsResponse

}

class MovieRepositoryImpl(
    private var remote: RemoteDataSource
) : MovieRepository {

    override suspend fun fetchPopular(page: Int): PopularResponse = safeDataCall {
        remote.fetchPopular(page)
    }

    override suspend fun fetchNowPlaying(page: Int): NowPlayingResponse = safeDataCall {
        remote.fetchNowPlaying(page)
    }

    override suspend fun fetchMovieDetails(movieId: Int): MovieDetailsResponse = safeDataCall {
        remote.fetchMovieDetails(movieId)
    }

}