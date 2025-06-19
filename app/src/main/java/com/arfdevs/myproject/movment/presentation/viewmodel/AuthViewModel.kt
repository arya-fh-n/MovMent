package com.arfdevs.myproject.movment.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.domain.repository.FirebaseRepository
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import com.arfdevs.myproject.core.helper.DomainResult
import com.arfdevs.myproject.core.helper.UiState
import kotlinx.coroutines.launch

class AuthViewModel(
    private val movieRepo: MovieRepository,
    private val userRepo: UserRepository,
    private val firebaseRepo: FirebaseRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _registerState = MutableLiveData<UiState<Boolean>>()
    val registerState: LiveData<UiState<Boolean>> get() = _registerState

    private val _loginState = MutableLiveData<UiState<Boolean>>()
    val loginState: LiveData<UiState<Boolean>> get() = _loginState

    private val _updateUsernameState = MutableLiveData<UiState<Boolean>>()
    val updateUsernameState: LiveData<UiState<Boolean>> get() = _updateUsernameState

    private val _logoutState = MutableLiveData<UiState<Boolean>>()
    val logoutState: LiveData<UiState<Boolean>> get() = _logoutState

    fun registerUser(user: User) = viewModelScope.launch(dispatcher.io) {
        val state = when (val result = userRepo.createUser(user)) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error(
                message = "Something went wrong. Please try again later.",
                errorCode = -1,
                data = null
            )
        }
        _registerState.postValue(state)
    }

    fun loginUser(user: User) = viewModelScope.launch(dispatcher.io) {
        val state = when (val result = userRepo.signInUser(user)) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error(
                message = "Login failed. Please check your credentials.",
                errorCode = -1,
                data = null
            )
        }
        _loginState.postValue(state)
    }

    fun updateUsername(username: String) = viewModelScope.launch(dispatcher.io) {
        val state = when (val result = userRepo.updateUsername(username)) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error(
                message = "Failed to update username. Please try again later.",
                errorCode = -1,
                data = null
            )
        }
        _updateUsernameState.postValue(state)
    }

    fun logoutUser() = viewModelScope.launch(dispatcher.io) {
        userRepo.signOutUser()
        _logoutState.postValue(UiState.Success(true))
    }

    fun logEvent(eventName: String, bundle: Bundle) {
        firebaseRepo.logEvent(eventName, bundle)
    }

    fun deleteAllWishlistItem() = viewModelScope.launch(dispatcher.io) {
        val userId = userRepo.getUID()
        if (userId.isNotEmpty()) {
            movieRepo.deleteAllWishlistItem(userId)
        }
    }

}
