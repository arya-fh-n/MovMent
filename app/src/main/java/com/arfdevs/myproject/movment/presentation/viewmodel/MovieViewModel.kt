package com.arfdevs.myproject.movment.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.domain.model.MovieDetailsModel
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.arfdevs.myproject.core.helper.UiState
import kotlinx.coroutines.launch

class MovieViewModel(private val useCase: AppUseCase) : ViewModel() {

    private var _responseDetails = MutableLiveData<UiState<MovieDetailsModel>>()
    val responseDetails: LiveData<UiState<MovieDetailsModel>> = _responseDetails

    private var _isFavorite = MutableLiveData<Int>()
    val isFavorite: LiveData<Int> = _isFavorite

    private var wishlistModel: WishlistModel? = null

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
            val fav = useCase.checkFavorite(movieId)
            Log.d("ViewModel", "checkFavorite: for $movieId, isFavorite: $fav")
            _isFavorite.value = useCase.checkFavorite(movieId)
        }
    }

    fun insertWishlistMovie() {
        viewModelScope.launch {
            wishlistModel?.let { wishlist ->
                Log.d("ViewModel", "insertWishlist: for $wishlist")
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


}