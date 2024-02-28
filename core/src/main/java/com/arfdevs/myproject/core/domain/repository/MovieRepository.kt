package com.arfdevs.myproject.core.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.arfdevs.myproject.core.data.local.datasource.LocalDataSource
import com.arfdevs.myproject.core.data.local.db.entity.WishlistEntity
import com.arfdevs.myproject.core.data.remote.datasource.RemoteDataSource
import com.arfdevs.myproject.core.data.remote.responses.MovieDetailsResponse
import com.arfdevs.myproject.core.data.remote.responses.NowPlayingResponse
import com.arfdevs.myproject.core.data.remote.responses.PopularResponse
import com.arfdevs.myproject.core.helper.safeDataCall

interface MovieRepository {

    suspend fun fetchPopular(page: Int): PopularResponse

    suspend fun fetchNowPlaying(page: Int): NowPlayingResponse

    suspend fun fetchMovieDetails(movieId: Int): MovieDetailsResponse

    suspend fun insertWishlistMovie(wishlist: WishlistEntity)

    fun getWishlist(userId: String): LiveData<List<WishlistEntity>>

    suspend fun checkFavorite(movieId: Int): Int

    suspend fun deleteWishlistMovie(wishlist: WishlistEntity)

}

class MovieRepositoryImpl(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
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

    override suspend fun insertWishlistMovie(wishlist: WishlistEntity) {
        local.insertWishlistMovie(wishlist)
    }

    override fun getWishlist(userId: String): LiveData<List<WishlistEntity>> = local.gethWishlistMovie(userId)


    override suspend fun checkFavorite(movieId: Int): Int = safeDataCall {
        val fav = local.checkFavorite(movieId)
        Log.d("Repository", "checkFavorite: for $movieId, isFavorite: $fav")
        fav
    }

    override suspend fun deleteWishlistMovie(wishlist: WishlistEntity) {
        local.deleteWishlistMovie(wishlist)
    }

}