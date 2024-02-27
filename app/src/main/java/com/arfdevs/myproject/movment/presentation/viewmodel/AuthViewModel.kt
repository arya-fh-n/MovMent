package com.arfdevs.myproject.movment.presentation.viewmodel

import android.util.Log
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

    private val _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: LiveData<FirebaseUser> = _currentUser

    fun registerUser(user: User) = runBlocking {
        val state = useCase.createUser(user)
        Log.d("ViewModel", "createUser: $state")
        state
    }

//    fun loginUser(user: User) {
//        viewModelScope.launch {
//            _loginResult.value = useCase.signInUser(user).value
//        }
//    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = useCase.getCurrentUser()
        }
    }


}