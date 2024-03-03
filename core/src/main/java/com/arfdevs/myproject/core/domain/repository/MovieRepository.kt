package com.arfdevs.myproject.core.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.arfdevs.myproject.core.data.local.datasource.LocalDataSource
import com.arfdevs.myproject.core.data.local.db.entity.CartEntity
import com.arfdevs.myproject.core.data.local.db.entity.WishlistEntity
import com.arfdevs.myproject.core.data.remote.datasource.RemoteDataSource
import com.arfdevs.myproject.core.data.remote.responses.MovieDetailsResponse
import com.arfdevs.myproject.core.data.remote.responses.NowPlayingResponse
import com.arfdevs.myproject.core.data.remote.responses.PopularResponse
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.helper.safeDataCall
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun fetchPopular(page: Int): PopularResponse

    suspend fun fetchNowPlaying(page: Int): NowPlayingResponse

    suspend fun fetchMovieDetails(movieId: Int): MovieDetailsResponse

    suspend fun fetchSearch(query: String): LiveData<PagingData<SearchModel>>

    suspend fun insertWishlistMovie(wishlist: WishlistEntity)

    fun getWishlist(userId: String): LiveData<List<WishlistEntity>>

    suspend fun checkFavorite(movieId: Int): Int

    suspend fun deleteWishlistMovie(wishlist: WishlistEntity)

    suspend fun deleteAllWishlistItem(userId: String)

    suspend fun insertCartMovie(cart: CartEntity)

    fun getCartList(userId: String): LiveData<List<CartEntity?>>

    fun getCartItemById(movieId: Int, userId: String): Flow<CartEntity?>

    suspend fun deleteCartItem(cart: CartEntity)

    suspend fun deleteAllCartItem()

}

class MovieRepositoryImpl(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource,
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

    override suspend fun fetchSearch(query: String): LiveData<PagingData<SearchModel>> =
        safeDataCall {
            remote.fetchSearch(query)
        }

    override suspend fun insertWishlistMovie(wishlist: WishlistEntity) {
        local.insertWishlistMovie(wishlist)
    }

    override fun getWishlist(userId: String): LiveData<List<WishlistEntity>> =
        local.getWishlistMovie(userId)


    override suspend fun checkFavorite(movieId: Int): Int = safeDataCall {
        local.checkFavorite(movieId)
    }

    override suspend fun deleteWishlistMovie(wishlist: WishlistEntity) {
        local.deleteWishlistMovie(wishlist)
    }

    override suspend fun deleteAllWishlistItem(userId: String) {
        local.deleteAllWishlist(userId)
    }

    override suspend fun insertCartMovie(cart: CartEntity) {
        local.insertCartMovie(cart)
    }

    override fun getCartList(userId: String): LiveData<List<CartEntity?>> =
        local.getCartList(userId)

    override fun getCartItemById(movieId: Int, userId: String): Flow<CartEntity?> =
        local.getCartItemById(movieId, userId)


    override suspend fun deleteCartItem(cart: CartEntity) {
        local.deleteCartItem(cart)
    }

    override suspend fun deleteAllCartItem() {
        local.deleteAllCart()
    }


}