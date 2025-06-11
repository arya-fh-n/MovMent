package com.arfdevs.myproject.movment.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.liveData
import com.arfdevs.myproject.core.domain.model.CartModel
import com.arfdevs.myproject.core.domain.model.MovieDetailsModel
import com.arfdevs.myproject.core.domain.model.MovieTransactionModel
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.domain.repository.FirebaseRepository
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.helper.Constants
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import com.arfdevs.myproject.core.helper.DomainResult
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.core.helper.orZero
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieViewModel(
    private val movieRepo: MovieRepository,
    private val userRepo: UserRepository,
    private val firebaseRepo: FirebaseRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    init {
        getUserID()
    }

    private var _responseDetails = MutableLiveData<UiState<MovieDetailsModel>>()
    val responseDetails: LiveData<UiState<MovieDetailsModel>> = _responseDetails

    private var _isFavorite = MutableLiveData<Int>()
    val isFavorite: LiveData<Int> = _isFavorite

    private var tempWishlistModel: WishlistModel = WishlistModel()
    private var tempCardModel: CartModel = CartModel()

    private val _cartItemById = MutableLiveData<CartModel?>()
    val cartItemById: LiveData<CartModel?> = _cartItemById

    private val _wishlist = MutableLiveData<List<WishlistModel>>()
    val wishlist: LiveData<List<WishlistModel>> get() = _wishlist

    private val _cartList = MutableLiveData<List<CartModel>>()
    val cartList: LiveData<List<CartModel>> get() = _cartList

    private val _transactionHistory = MutableLiveData<List<MovieTransactionModel>>()
    val transactionHistory: LiveData<List<MovieTransactionModel>> get() = _transactionHistory

    private val _movieSearch = MutableLiveData<PagingData<SearchModel>>()
    val movieSearch: LiveData<PagingData<SearchModel>> get() = _movieSearch

    private val _insertResult = MutableStateFlow(false)
    val insertResult = _insertResult.asStateFlow()

    private val _tokenBalance = MutableStateFlow(0)
    val tokenBalance = _tokenBalance.asStateFlow()

    private val _uid = MutableStateFlow("")
    val uid = _uid.asStateFlow()

    fun getMovieDetails(movieId: Int) = viewModelScope.launch(dispatcher.io) {
        _responseDetails.postValue(UiState.Loading)

        val state = try {
            val uiState = when (val result = movieRepo.fetchMovieDetails(movieId)) {
                is DomainResult.EmptyState -> UiState.Empty
                is DomainResult.ErrorState -> UiState.Error(
                    result.message.orEmpty(),
                    result.responseStatusCode.orZero()
                )

                DomainResult.NetworkError -> UiState.Error(
                    Constants.MSG_NETWORK_ERROR,
                    Constants.CODE_NETWORK_ERROR
                )

                is DomainResult.TechnicalError -> UiState.Error(
                    Constants.MSG_TECHNICAL_ERROR,
                    result.code
                )

                is DomainResult.Success -> UiState.Success(result.data)
            }
            uiState
        } catch (e: Throwable) {
            UiState.Error(e.message.orEmpty())
        }

        _responseDetails.postValue(state)
    }

    fun checkFavorite(movieId: Int) = viewModelScope.launch(dispatcher.io) {
        val isFavorite = async { movieRepo.checkFavorite(movieId) }

        withContext(dispatcher.main) {
            _isFavorite.value = isFavorite.await()
        }
    }

    fun insertWishlistMovie() = viewModelScope.launch(dispatcher.io) {
        movieRepo.insertWishlistMovie(tempWishlistModel)
    }

    fun deleteWishlistFromDetail() = viewModelScope.launch(dispatcher.io) {
        movieRepo.deleteWishlistMovie(tempWishlistModel)
    }

    fun deleteWishlist(wishlist: WishlistModel) = viewModelScope.launch {
        movieRepo.deleteWishlistMovie(wishlist)
    }

    fun getWishlist() = viewModelScope.launch(dispatcher.io) {
        val userId = async { userRepo.getUID() }
        val result = movieRepo.getWishlist(userId.await())
        _wishlist.postValue(result)
    }

    fun setWishlistModel(wishlistModel: WishlistModel) {
        this.tempWishlistModel = wishlistModel
    }

    fun setCartModel(cart: CartModel) {
        tempCardModel = cart
    }

    fun insertToCart(cart: CartModel) = viewModelScope.launch(dispatcher.io) {
        movieRepo.insertCartMovie(cart)
    }

    fun insertToCartFromDetail() = viewModelScope.launch(dispatcher.io) {
        movieRepo.insertCartMovie(tempCardModel)
    }

    fun getCartList() = viewModelScope.launch(dispatcher.io) {
        val userId = async { userRepo.getUID() }
        val result = movieRepo.getCartList(userId.await())
        _cartList.postValue(result)
    }

    fun getCartById(movieId: Int, userId: String) = viewModelScope.launch(dispatcher.io) {
        val result = movieRepo.getCartItemById(movieId, userId)
        _cartItemById.postValue(result)
    }

    fun deleteCart(cart: CartModel) = viewModelScope.launch(dispatcher.io) {
        movieRepo.deleteCartItem(cart)
    }

    fun removeAllCartItem() = viewModelScope.launch(dispatcher.io) {
        movieRepo.deleteAllCartItem()
    }

    fun getTokenBalance() = viewModelScope.launch(dispatcher.io) {
        val userId = async { userRepo.getUID() }
        val result = firebaseRepo.getTokenBalance(userId.await())
        _tokenBalance.update {
            result
        }
    }

    fun insertTransactionModel(
        transaction: MovieTransactionModel,
        userId: String
    ) = viewModelScope.launch(dispatcher.io) {
        val result = firebaseRepo.insertMovieTransaction(transaction, userId)
        _insertResult.update { result }
    }

    fun getTransactionHistory() = viewModelScope.launch(dispatcher.io) {
        val userId = async { userRepo.getUID() }
        val movieTransaction = firebaseRepo.getMovieTransaction(userId.await())
        _transactionHistory.postValue(movieTransaction)
    }

    fun getUserID() = viewModelScope.launch(dispatcher.io) {
        val userId = userRepo.getUID()
        _uid.update {
            userId
        }
    }

    fun searchMovies(query: String) = viewModelScope.launch(dispatcher.io) {
        val result = movieRepo.fetchSearch(query)
        _movieSearch.postValue(result.liveData.value)
    }

    fun logEvent(eventName: String, bundle: Bundle) = viewModelScope.launch(dispatcher.io) {
        firebaseRepo.logEvent(eventName, bundle)
    }

}
