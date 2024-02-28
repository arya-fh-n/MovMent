package com.arfdevs.myproject.movment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.domain.model.MovieDetailsModel
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.arfdevs.myproject.core.helper.UiState
import kotlinx.coroutines.launch

class MovieViewModel(private val useCase: AppUseCase) : ViewModel() {

    private var _responseDetails = MutableLiveData<UiState<MovieDetailsModel>>()
    val responseDetails: LiveData<UiState<MovieDetailsModel>> = _responseDetails

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

}