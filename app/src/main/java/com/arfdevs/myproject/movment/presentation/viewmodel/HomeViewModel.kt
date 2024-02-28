package com.arfdevs.myproject.movment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.arfdevs.myproject.movment.presentation.helper.Constants
import com.arfdevs.myproject.movment.presentation.helper.Constants.INDONESIAN
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun saveLanguage(locale: String) {
        viewModelScope.launch {
            useCase.saveLanguage(locale)
        }
    }

    fun getLanguage() {
        viewModelScope.launch {
            _language.value.run {
                useCase.getLanguage().equals(INDONESIAN, true)
            }
        }
    }

    fun saveTheme(theme: Boolean) {
        viewModelScope.launch {
            useCase.saveTheme(theme)
        }
    }

    fun getTheme() {
        viewModelScope.launch {
            _theme.value.run {
                useCase.getTheme()
            }
        }
    }

}