package com.arfdevs.myproject.movment.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class HomeViewModel(private val useCase: AppUseCase): ViewModel() {

    private val _responsePopular: MutableLiveData<List<PopularModel>> = MutableLiveData()
    val responsePopular: LiveData<List<PopularModel>> = _responsePopular

    private val _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: LiveData<FirebaseUser> = _currentUser

    fun getPopularMovies(page: Int) {
        viewModelScope.launch {
            _responsePopular.value = useCase.getPopular(page)
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = useCase.getCurrentUser()
        }
    }

}