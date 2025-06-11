package com.arfdevs.myproject.core.domain.repository

import androidx.paging.Pager
import com.arfdevs.myproject.core.domain.model.CartModel
import com.arfdevs.myproject.core.domain.model.MovieDetailsModel
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.helper.DomainResult

interface MovieRepository {

    suspend fun fetchPopular(page: Int): DomainResult<PopularModel>

    suspend fun fetchNowPlaying(page: Int): DomainResult<NowPlayingModel>

    suspend fun fetchMovieDetails(movieId: Int): DomainResult<MovieDetailsModel>

    suspend fun fetchSearch(query: String): Pager<Int, SearchModel>

    suspend fun insertWishlistMovie(wishlist: WishlistModel)

    suspend fun getWishlist(userId: String): List<WishlistModel>

    suspend fun checkFavorite(movieId: Int): Int

    suspend fun deleteWishlistMovie(wishlist: WishlistModel)

    suspend fun deleteAllWishlistItem(userId: String)

    suspend fun insertCartMovie(cart: CartModel)

    suspend fun getCartList(userId: String): List<CartModel>

    suspend fun getCartItemById(movieId: Int, userId: String): CartModel

    suspend fun deleteCartItem(cart: CartModel)

    suspend fun deleteAllCartItem()

}
