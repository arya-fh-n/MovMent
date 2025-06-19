package com.arfdevs.myproject.movment.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.domain.model.CartModel
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.domain.model.SessionModel
import com.arfdevs.myproject.core.domain.repository.FirebaseRepository
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.domain.usecase.GetSessionUseCase
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import com.arfdevs.myproject.core.helper.DataMapper.toNowPlayingList
import com.arfdevs.myproject.core.helper.DataMapper.toPopularList
import com.arfdevs.myproject.core.helper.DataMapper.toSplashState
import com.arfdevs.myproject.core.helper.DomainResult
import com.arfdevs.myproject.core.helper.SplashState
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.movment.presentation.helper.Constants.INDONESIAN
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepo: MovieRepository,
    private val userRepo: UserRepository,
    private val firebaseRepo: FirebaseRepository,
    private val sessionUseCase: GetSessionUseCase,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _popularList: MutableLiveData<UiState<List<PopularModel>>> = MutableLiveData()
    val popularList: LiveData<UiState<List<PopularModel>>> = _popularList

    private val _nowPlayingList: MutableLiveData<UiState<List<NowPlayingModel>>> = MutableLiveData()
    val nowPlayingList: LiveData<UiState<List<NowPlayingModel>>> = _nowPlayingList

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _language: MutableLiveData<Boolean> = MutableLiveData()
    val language: LiveData<Boolean> = _language

    private val _theme: MutableLiveData<Boolean> = MutableLiveData()
    val theme: LiveData<Boolean> = _theme

    private val _onboardingState =
        MutableLiveData<SplashState<SessionModel>>()
    val onboardingState: LiveData<SplashState<SessionModel>> = _onboardingState

    private val _tokenBalance = MutableLiveData<UiState<Int>>()
    val tokenBalance: LiveData<UiState<Int>> = _tokenBalance

    fun getPopularMovies(page: Int) = viewModelScope.launch(dispatcher.io) {
        val state = when (val result = movieRepo.fetchPopular(page)) {
            is DomainResult.Success -> {
                val mappedResult = result.data.results.toPopularList()
                UiState.Success(mappedResult)
            }

            else -> UiState.Error("Something went wrong. Please try again later.")
        }
        _popularList.postValue(state)
    }

    fun getNowPlaying(page: Int) = viewModelScope.launch(dispatcher.io) {
        val state = when (val result = movieRepo.fetchNowPlaying(page)) {
            is DomainResult.Success -> {
                val mappedResult = result.data.results.toNowPlayingList()
                UiState.Success(mappedResult)
            }

            else -> UiState.Error("Something went wrong. Please try again later.")
        }
        _nowPlayingList.postValue(state)
    }

    fun getCurrentUser() = viewModelScope.launch(dispatcher.io) {
        when (val state = userRepo.fetchCurrentUser()) {
            is DomainResult.Success -> {
                _currentUser.value = state.data
            }

            else -> {
                _currentUser.value = null
            }
        }
    }

    fun saveOnboardingState(state: Boolean) = viewModelScope.launch(dispatcher.io) {
        userRepo.saveOnboardingState(state)
    }

    fun getOnboardingState() = viewModelScope.launch(dispatcher.io) {
        val splashState = sessionUseCase().toSplashState()
        _onboardingState.postValue(splashState)
    }

    fun saveUID(uid: String) = viewModelScope.launch(dispatcher.io) {
        userRepo.saveUID(uid)
    }

    fun saveLanguage(locale: String) = viewModelScope.launch(dispatcher.io) {
        userRepo.saveLanguage(locale)
    }

    fun getLanguage() = viewModelScope.launch(dispatcher.io) {
        _language.postValue(userRepo.getLanguage().equals(INDONESIAN, true))
    }

    fun saveTheme(theme: Boolean) = viewModelScope.launch(dispatcher.io) {
        userRepo.saveTheme(theme)
    }

    fun getTheme() = viewModelScope.launch {
        _theme.postValue(userRepo.getTheme())
    }

    fun logEvent(eventName: String, bundle: Bundle) = viewModelScope.launch {
        firebaseRepo.logEvent(eventName, bundle)
    }

    fun getTokenBalance() = viewModelScope.launch(dispatcher.io) {
        val userId = userRepo.getUID()
        val state = when (val result = firebaseRepo.getTokenBalance(userId)) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error("Something went wrong. Please try again later.")
        }
        _tokenBalance.postValue(state)
    }

    fun insertToCart(cart: CartModel) = viewModelScope.launch {
        val userId = userRepo.getUID()
        movieRepo.insertCartMovie(cart.copy(userId = userId))
    }

}
