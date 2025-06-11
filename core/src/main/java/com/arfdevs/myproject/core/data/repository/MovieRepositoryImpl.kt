package com.arfdevs.myproject.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.arfdevs.myproject.core.data.local.db.Dao
import com.arfdevs.myproject.core.data.remote.ApiEndpoint
import com.arfdevs.myproject.core.data.remote.datasource.SearchPagingSource
import com.arfdevs.myproject.core.domain.model.CartModel
import com.arfdevs.myproject.core.domain.model.MovieDetailsModel
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import com.arfdevs.myproject.core.helper.DataMapper.toEntityData
import com.arfdevs.myproject.core.helper.DataMapper.toLocalCartList
import com.arfdevs.myproject.core.helper.DataMapper.toLocalWishlistList
import com.arfdevs.myproject.core.helper.DataMapper.toUIData
import com.arfdevs.myproject.core.helper.DomainResult
import com.arfdevs.myproject.core.helper.processResponse
import kotlinx.coroutines.withContext

// TODO Change return model with new domain model
class MovieRepositoryImpl(
    private val api: ApiEndpoint,
    private val dao: Dao,
    private val dispatcher: CoroutinesDispatcherProvider
) : MovieRepository {

    override suspend fun fetchPopular(
        page: Int
    ): DomainResult<PopularModel> = withContext(dispatcher.io) {
        val result = api.fetchPopularMovies(page)

        return@withContext processResponse(result) {
            DomainResult.Success(PopularModel())
        }
    }

    override suspend fun fetchNowPlaying(
        page: Int
    ): DomainResult<NowPlayingModel> = withContext(dispatcher.io) {
        val result = api.fetchPopularMovies(page)
        return@withContext processResponse(result) {
            DomainResult.Success(NowPlayingModel())
        }
    }

    override suspend fun fetchMovieDetails(
        movieId: Int
    ): DomainResult<MovieDetailsModel> = withContext(dispatcher.io) {
        val result = api.fetchMovieDetails(movieId)

        return@withContext processResponse(result) {
            DomainResult.Success(MovieDetailsModel())
        }
    }

    override suspend fun fetchSearch(
        query: String
    ): Pager<Int, SearchModel> = withContext(dispatcher.io) {
        Pager(
            config = PagingConfig(
                enablePlaceholders = true,
                pageSize = 20,
                initialLoadSize = 10,
                prefetchDistance = 1
            ),
            pagingSourceFactory = {
                SearchPagingSource(api = api, query = query)
            }
        )
    }

    override suspend fun insertWishlistMovie(
        wishlist: WishlistModel
    ) = withContext(dispatcher.io) {
        dao.insertWishlistMovie(wishlist.toEntityData())
    }

    override suspend fun getWishlist(
        userId: String
    ): List<WishlistModel> = withContext(dispatcher.io) {
        val result = dao.getWishlistMovie(userId)
        return@withContext result.toLocalWishlistList()
    }

    override suspend fun checkFavorite(
        movieId: Int
    ): Int = withContext(dispatcher.io) {
        val isFavorite = dao.checkFavorite(movieId)
        return@withContext isFavorite
    }

    override suspend fun deleteWishlistMovie(wishlist: WishlistModel) = withContext(dispatcher.io) {
        dao.deleteWishlistMovie(wishlist.toEntityData())
    }

    override suspend fun deleteAllWishlistItem(userId: String) = withContext(dispatcher.io) {
        dao.deleteAllWishlistItem(userId)
    }

    override suspend fun insertCartMovie(cart: CartModel) = withContext(dispatcher.io) {
        dao.insertCart(cart.toEntityData())
    }

    override suspend fun getCartList(
        userId: String
    ): List<CartModel> = withContext(dispatcher.io) {
        val cartList = dao.getCartList(userId)
        return@withContext cartList.toLocalCartList()
    }

    override suspend fun getCartItemById(
        movieId: Int,
        userId: String
    ): CartModel = withContext(dispatcher.io) {
        val cartItem = dao.getCartItemById(movieId, userId)
        return@withContext cartItem?.toUIData() ?: CartModel()
    }

    override suspend fun deleteCartItem(cart: CartModel) = withContext(dispatcher.io) {
        dao.deleteCartItem(cart.toEntityData())
    }

    override suspend fun deleteAllCartItem() = withContext(dispatcher.io) {
        dao.deleteAllCartItem()
    }

}

