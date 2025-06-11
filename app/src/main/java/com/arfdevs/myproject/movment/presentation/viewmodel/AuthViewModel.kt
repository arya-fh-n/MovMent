package com.arfdevs.myproject.movment.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.domain.repository.FirebaseRepository
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthViewModel(
    private val movieRepo: MovieRepository,
    private val userRepo: UserRepository,
    private val firebaseRepo: FirebaseRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    fun registerUser(user: User) = runBlocking {
        useCase.createUser(user)
    }

    fun loginUser(user: User) = runBlocking {
        useCase.signInUser(user)
    }

    fun updateUsername(username: String) = runBlocking {
        useCase.updateUsername(username)
    }

    fun logoutUser() = runBlocking {
        useCase.signOutUser()
    }

    fun logEvent(eventName: String, bundle: Bundle) {
        useCase.logEvent(eventName, bundle)
    }

    fun getUID(): String = useCase.getUID()

    fun deleteAllWishlistItem(userId: String) {
        viewModelScope.launch {
            useCase.deleteAllWishlistItem(userId)
        }
    }

}
