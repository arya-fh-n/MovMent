package com.arfdevs.myproject.movment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthViewModel(private val useCase: AppUseCase) : ViewModel() {

    fun registerUser(user: User) = runBlocking {
        useCase.createUser(user)
    }

    fun loginUser(user: User) = runBlocking {
        useCase.signInUser(user)
    }

    fun updateUsername(username: String) = runBlocking {
        useCase.updateUsername(username)
    }

}