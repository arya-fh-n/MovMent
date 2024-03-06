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
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.arfdevs.myproject.core.helper.DataMapper.toSplashState
import com.arfdevs.myproject.core.helper.SplashState
import com.arfdevs.myproject.movment.presentation.helper.Constants.INDONESIAN
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val useCase: AppUseCase) : ViewModel() {

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

    fun getPopularMovies(page: Int) {
        viewModelScope.launch {
            _responsePopular.value = useCase.getPopular(page)
        }
    }

    fun getNowPlaying(page: Int) {
        viewModelScope.launch {
            _responseNowPlaying.value = useCase.getNowPlaying(page)
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = useCase.getCurrentUser()
        }
    }

    fun saveOnboardingState(state: Boolean) {
        useCase.saveOnboardingState(state)
    }

    fun getOnboardingState() {
        viewModelScope.launch {
            _onboardingState.value = useCase.sessionModel().toSplashState()
        }
    }

    fun saveUID(uid: String) {
        useCase.saveUID(uid)
    }

    fun saveLanguage(locale: String) {
        viewModelScope.launch {
            useCase.saveLanguage(locale)
        }
    }

    fun getLanguage() {
        viewModelScope.launch {
            _language.value = useCase.getLanguage().equals(INDONESIAN, true)
        }
    }

    fun saveTheme(theme: Boolean) {
        viewModelScope.launch {
            useCase.saveTheme(theme)
        }
    }

    fun getTheme() {
        viewModelScope.launch {
            _theme.value = useCase.getTheme()
        }
    }

    fun logEvent(eventName: String, bundle: Bundle) {
        useCase.logEvent(eventName, bundle)
    }

    fun getTokenBalance(userId: String) = runBlocking {
        useCase.getTokenBalance(userId)
    }

    fun insertToCart(cart: CartModel) {
        viewModelScope.launch {
            useCase.insertCartMovie(cart)
        }
    }

}
