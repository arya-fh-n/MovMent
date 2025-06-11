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
import com.arfdevs.myproject.core.helper.DataMapper.toEntityData
import com.arfdevs.myproject.core.helper.DataMapper.toNowPlayingList
import com.arfdevs.myproject.core.helper.DataMapper.toPopularList
import com.arfdevs.myproject.core.helper.DataMapper.toSplashState
import com.arfdevs.myproject.core.helper.NoConnectivityException
import com.arfdevs.myproject.core.helper.SplashState
import com.arfdevs.myproject.movment.presentation.helper.Constants.INDONESIAN
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(
    private val movieRepo: MovieRepository,
    private val userRepo: UserRepository,
    private val firebaseRepo: FirebaseRepository,
    private val sessionUseCase: GetSessionUseCase,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _responsePopular: MutableLiveData<List<PopularModel>> = MutableLiveData()
    val responsePopular: LiveData<List<PopularModel>> = _responsePopular

    private val _responseNowPlaying: MutableLiveData<List<NowPlayingModel>> = MutableLiveData()
    val responseNowPlaying: LiveData<List<NowPlayingModel>> = _responseNowPlaying

    private val _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: LiveData<FirebaseUser> = _currentUser

    private val _language: MutableLiveData<Boolean> = MutableLiveData()
    val language: LiveData<Boolean> = _language

    private val _theme: MutableLiveData<Boolean> = MutableLiveData()
    val theme: LiveData<Boolean> = _theme

    private val _onboardingState =
        MutableLiveData<SplashState<SessionModel>>()
    val onboardingState: LiveData<SplashState<SessionModel>> = _onboardingState

    fun getPopularMovies(page: Int) = viewModelScope.launch(dispatcher.io) {
        try {
            val result = movieRepo.fetchPopular(page)
            val mappedResult = result.results.toPopularList()
            _responsePopular.postValue(mappedResult)
        } catch (e: NoConnectivityException) {
            e.printStackTrace()
            _responsePopular.value = emptyList()
        }
    }

    fun getNowPlaying(page: Int) = viewModelScope.launch(dispatcher.io) {
        val result = movieRepo.fetchNowPlaying(page)
        val mappedResult = result.results.toNowPlayingList()
        _responseNowPlaying.postValue(mappedResult)
    }

    fun getCurrentUser() = viewModelScope.launch {
        val user = async { userRepo.fetchCurrentUser() }
        _currentUser.value = user.await()
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

    fun getTokenBalance(userId: String) = viewModelScope.launch(dispatcher.io) {
        firebaseRepo.getTokenBalance(userId)
    }

    fun insertToCart(cart: CartModel) = viewModelScope.launch {
        movieRepo.insertCartMovie(cart.toEntityData())
    }

}
