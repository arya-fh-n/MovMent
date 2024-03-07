package com.arfdevs.myproject.core.domain.usecase

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.PagingData
import com.arfdevs.myproject.core.data.remote.responses.PaymentMethodsResponse
import com.arfdevs.myproject.core.data.remote.responses.TokenTopupResponse
import com.arfdevs.myproject.core.domain.model.CartModel
import com.arfdevs.myproject.core.domain.model.MovieDetailsModel
import com.arfdevs.myproject.core.domain.model.MovieTransactionModel
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PaymentTypeModel
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.domain.model.SessionModel
import com.arfdevs.myproject.core.domain.model.TokenTopupModel
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.domain.repository.FirebaseRepository
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.helper.DataMapper.toEntityData
import com.arfdevs.myproject.core.helper.DataMapper.toLocalCartList
import com.arfdevs.myproject.core.helper.DataMapper.toLocalWishlistList
import com.arfdevs.myproject.core.helper.DataMapper.toNowPlayingList
import com.arfdevs.myproject.core.helper.DataMapper.toPopularList
import com.arfdevs.myproject.core.helper.DataMapper.toUIData
import com.arfdevs.myproject.core.helper.NoConnectivityException
import com.arfdevs.myproject.core.helper.SourceResult
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.core.helper.safeDataCall
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AppUseCase {

    suspend fun getPopular(page: Int): List<PopularModel>

    suspend fun getNowPlaying(page: Int): List<NowPlayingModel>

    suspend fun getMovieDetails(movieId: Int): MovieDetailsModel

    suspend fun insertWishlistMovie(wishlist: WishlistModel)

    fun getWishlist(userId: String): LiveData<List<WishlistModel>>

    suspend fun checkFavorite(movieId: Int): Int

    suspend fun deleteWishlistMovie(wishlist: WishlistModel)

    suspend fun deleteAllWishlistItem(userId: String)

    suspend fun insertCartMovie(cart: CartModel)

    fun getCartList(userId: String): LiveData<List<CartModel?>>

    fun getCartItemById(movieId: Int, userId: String): Flow<CartModel?>

    suspend fun deleteCartItem(cart: CartModel)

    suspend fun deleteAllCartItem()

    suspend fun fetchSearch(query: String): LiveData<PagingData<SearchModel>>

    suspend fun getConfigTokenTopupList(): Flow<List<TokenTopupModel>>

    suspend fun updateConfigTokenTopupList(): Flow<Boolean>

    suspend fun getConfigPaymentMethodsList(): Flow<List<PaymentTypeModel>>

    suspend fun updateConfigPaymentMethodsList(): Flow<Boolean>

    fun logEvent(eventName: String, bundle: Bundle)

    suspend fun insertTokenTransaction(
        transactionModel: TokenTransactionModel,
        userId: String
    ): Flow<Boolean>

    suspend fun getTokenBalance(userId: String): Flow<Int>

    suspend fun insertMovieTransaction(
        movieTransaction: MovieTransactionModel,
        userId: String
    ): Flow<Boolean>

    suspend fun getMovieTransaction(userId: String): Flow<List<MovieTransactionModel?>>

    suspend fun createUser(user: User): Flow<UiState<Boolean>>

    suspend fun signInUser(user: User): Flow<UiState<Boolean>>

    suspend fun updateUsername(username: String): Flow<UiState<Boolean>>

    suspend fun getCurrentUser(): FirebaseUser?

    suspend fun sessionModel(): SessionModel

    suspend fun signOutUser()

    fun saveOnboardingState(state: Boolean)

    fun getLanguage(): String

    fun saveLanguage(value: String)

    fun getTheme(): Boolean

    fun saveTheme(value: Boolean)

    fun getUID(): String

    fun saveUID(value: String)

}

class AppInteractor(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
    private val firebaseRepository: FirebaseRepository
) : AppUseCase {

    override suspend fun getPopular(page: Int): List<PopularModel> {
        return try {
            safeDataCall {
                movieRepository.fetchPopular(page).results.toPopularList()
            }
        } catch (e: NoConnectivityException) {
            throw e
        }
    }

    override suspend fun getNowPlaying(page: Int): List<NowPlayingModel> = safeDataCall {
        movieRepository.fetchNowPlaying(page).results.toNowPlayingList()
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsModel = safeDataCall {
        movieRepository.fetchMovieDetails(movieId).toUIData()
    }

    override suspend fun insertWishlistMovie(wishlist: WishlistModel) {
        movieRepository.insertWishlistMovie(wishlist.toEntityData())
    }

    override fun getWishlist(userId: String): LiveData<List<WishlistModel>> =
        movieRepository.getWishlist(userId).map { list ->
            list.toLocalWishlistList()
        }

    override suspend fun checkFavorite(movieId: Int): Int = safeDataCall {
        movieRepository.checkFavorite(movieId)
    }

    override suspend fun deleteWishlistMovie(wishlist: WishlistModel) {
        movieRepository.deleteWishlistMovie(wishlist.toEntityData())
    }

    override suspend fun deleteAllWishlistItem(userId: String) {
        movieRepository.deleteAllWishlistItem(userId)
    }

    override suspend fun insertCartMovie(cart: CartModel) {
        movieRepository.insertCartMovie(cart.toEntityData())
    }

    override fun getCartList(userId: String): LiveData<List<CartModel?>> =
        movieRepository.getCartList(userId).map { cartList ->
            cartList.toLocalCartList()
        }

    override fun getCartItemById(movieId: Int, userId: String): Flow<CartModel?> =
        movieRepository.getCartItemById(movieId, userId).map { cart ->
            cart?.toUIData()
        }


    override suspend fun deleteCartItem(cart: CartModel) {
        movieRepository.deleteCartItem(cart.toEntityData())
    }

    override suspend fun deleteAllCartItem() {
        movieRepository.deleteAllCartItem()
    }

    override suspend fun fetchSearch(query: String): LiveData<PagingData<SearchModel>> =
        safeDataCall {
            movieRepository.fetchSearch(query)
        }

    override suspend fun getConfigTokenTopupList(): Flow<List<TokenTopupModel>> =
        safeDataCall {
            firebaseRepository.getConfigTokenTopupList().map { data ->
                val response = Gson().fromJson(data, TokenTopupResponse::class.java)
                response.map {
                    it.toUIData()
                }
            }
        }


    override suspend fun updateConfigTokenTopupList(): Flow<Boolean> = safeDataCall {
        firebaseRepository.updateConfigTokenTopupList()
    }

    override suspend fun getConfigPaymentMethodsList(): Flow<List<PaymentTypeModel>> =
        safeDataCall {
            firebaseRepository.getConfigPaymentMethodsList().map { data ->
                val response = Gson().fromJson(data, PaymentMethodsResponse::class.java)
                response.data.map {
                    it.toUIData()
                }.toList()
            }
        }


    override suspend fun updateConfigPaymentMethodsList(): Flow<Boolean> = safeDataCall {
        firebaseRepository.updateConfigPaymentMethodsList()
    }

    override fun logEvent(eventName: String, bundle: Bundle) {
        firebaseRepository.logEvent(eventName, bundle)
    }

    override suspend fun insertTokenTransaction(
        transactionModel: TokenTransactionModel,
        userId: String
    ): Flow<Boolean> = safeDataCall {
        firebaseRepository.insertTokenTransaction(transactionModel, userId)
    }

    override suspend fun getTokenBalance(userId: String): Flow<Int> = safeDataCall {
        firebaseRepository.getTokenBalance(userId)
    }

    override suspend fun insertMovieTransaction(
        movieTransaction: MovieTransactionModel,
        userId: String
    ): Flow<Boolean> = safeDataCall {
        firebaseRepository.insertMovieTransaction(movieTransaction, userId)
    }

    override suspend fun getMovieTransaction(userId: String): Flow<List<MovieTransactionModel?>> =
        safeDataCall {
            firebaseRepository.getMovieTransaction(userId)
        }

    override suspend fun createUser(user: User): Flow<UiState<Boolean>> = safeDataCall {
        userRepository.createUser(user).map {
            when (it) {
                is SourceResult.Success -> {
                    UiState.Success(it.data)
                }

                is SourceResult.Error -> {
                    UiState.Error(it.throwable)
                }

                is SourceResult.Loading -> {
                    UiState.Loading
                }
            }
        }
    }

    override suspend fun signInUser(user: User): Flow<UiState<Boolean>> = safeDataCall {
        userRepository.signInUser(user).map {
            when (it) {
                is SourceResult.Success -> {
                    UiState.Success(it.data)
                }

                is SourceResult.Error -> {
                    UiState.Error(it.throwable)
                }

                is SourceResult.Loading -> {
                    UiState.Loading
                }
            }
        }
    }

    override suspend fun updateUsername(username: String): Flow<UiState<Boolean>> = safeDataCall {
        userRepository.updateUsername(username).map {
            when (it) {
                is SourceResult.Success -> {
                    UiState.Success(it.data)
                }

                is SourceResult.Error -> {
                    UiState.Error(it.throwable)
                }

                is SourceResult.Loading -> {
                    UiState.Loading
                }
            }
        }
    }

    override suspend fun getCurrentUser(): FirebaseUser? = safeDataCall {
        userRepository.fetchCurrentUser()
    }

    override suspend fun sessionModel(): SessionModel {
        val user = userRepository.fetchCurrentUser()
        val onboardingState = userRepository.getOnboardingState()

        return SessionModel(user?.displayName ?: "", user?.uid ?: "", onboardingState)
    }

    override suspend fun signOutUser() {
        userRepository.signOutUser()
    }


    override fun saveOnboardingState(state: Boolean) {
        userRepository.saveOnboardingState(state)
    }

    override fun getLanguage(): String = userRepository.getLanguage()

    override fun saveLanguage(value: String) {
        userRepository.saveLanguage(value)
    }

    override fun getTheme(): Boolean = userRepository.getTheme()

    override fun saveTheme(value: Boolean) {
        userRepository.saveTheme(value)
    }

    override fun getUID(): String = userRepository.getUID()

    override fun saveUID(value: String) {
        userRepository.saveUID(value)
    }

}