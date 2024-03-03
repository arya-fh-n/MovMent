package com.arfdevs.myproject.movment.presentation.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.domain.model.CartModel
import com.arfdevs.myproject.core.domain.model.MovieDetailsModel
import com.arfdevs.myproject.core.domain.model.MovieTransactionModel
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.arfdevs.myproject.core.helper.UiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MovieViewModel(private val useCase: AppUseCase) : ViewModel() {

    private var _responseDetails = MutableLiveData<UiState<MovieDetailsModel>>()
    val responseDetails: LiveData<UiState<MovieDetailsModel>> = _responseDetails

    private var _isFavorite = MutableLiveData<Int>()
    val isFavorite: LiveData<Int> = _isFavorite

    private var wishlistModel: WishlistModel? = null
    private var cartModel: CartModel? = null

    private val _cartItemById = MutableLiveData<CartModel?>()
    val cartItemById: LiveData<CartModel?> = _cartItemById

    fun getMovieDetails(movieId: Int) {
        _responseDetails.value = UiState.Loading

        viewModelScope.launch {
            try {
                val details = useCase.getMovieDetails(movieId)
                _responseDetails.value = UiState.Success(details)
            } catch (e: Throwable) {
                _responseDetails.value = UiState.Error(e)
            }
        }
    }

    fun checkFavorite(movieId: Int) {
        viewModelScope.launch {
            _isFavorite.value = useCase.checkFavorite(movieId)
        }
    }

    fun insertWishlistMovie() {
        viewModelScope.launch {
            wishlistModel?.let { wishlist ->
                useCase.insertWishlistMovie(wishlist)
            }
        }
    }

    fun deleteWishlistFromDetail() {
        viewModelScope.launch {
            wishlistModel?.let { wishlist ->
                useCase.deleteWishlistMovie(wishlist)
            }
        }
    }

    fun deleteWishlist(wishlist: WishlistModel) {
        viewModelScope.launch {
            useCase.deleteWishlistMovie(wishlist)
        }
    }

    fun getWishlist(userId: String) = useCase.getWishlist(userId)

    fun setWishlistModel(wishlistModel: WishlistModel) {
        this.wishlistModel = wishlistModel
    }

    fun setCartModel(cart: CartModel) {
        cartModel = cart
    }

    fun insertToCart(cart: CartModel) {
        viewModelScope.launch {
            useCase.insertCartMovie(cart)
        }
    }

    fun insertToCartFromDetail() {
        viewModelScope.launch {
            cartModel?.let { cart ->
                useCase.insertCartMovie(cart)
            }
        }
    }

    fun getCartList(userId: String) = useCase.getCartList(userId)

    fun getCartById(movieId: Int, userId: String) {
        viewModelScope.launch {
            useCase.getCartItemById(movieId, userId).collect { cartById ->
                _cartItemById.postValue(cartById)
            }
        }
    }

    fun deleteCart(cart: CartModel) {
        viewModelScope.launch {
            useCase.deleteCartItem(cart)
        }
    }

    fun removeAllCartItem() {
        viewModelScope.launch {
            useCase.deleteAllCartItem()
        }
    }

    fun getTokenBalance(userId: String) = runBlocking {
        useCase.getTokenBalance(userId)
    }

    fun insertTransactionModel(transaction: MovieTransactionModel, userId: String) = runBlocking {
        useCase.insertMovieTransaction(transaction, userId)
    }

    fun getTransactionHistory(userId: String) = runBlocking {
        useCase.getMovieTransaction(userId)
    }

    fun getUID(): String = useCase.getUID()

    fun searchMovies(query: String) = runBlocking {
        useCase.fetchSearch(query)
    }

    fun logEvent(eventName: String, bundle: Bundle) {
        useCase.logEvent(eventName, bundle)
    }

}