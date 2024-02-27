package com.arfdevs.myproject.core.domain.usecase

import android.util.Log
import androidx.lifecycle.LiveData
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.helper.DataMapper.toListData
import com.arfdevs.myproject.core.helper.SourceResult
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.core.helper.safeDataCall
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AppUseCase {

    suspend fun getPopular(page: Int): List<PopularModel>

    suspend fun createUser(user: User): Flow<UiState<Boolean>>

    suspend fun signInUser(user: User): LiveData<Boolean>

    suspend fun getCurrentUser(): FirebaseUser

}

class AppInteractor(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository
) : AppUseCase {

    override suspend fun getPopular(page: Int): List<PopularModel> = safeDataCall {
        movieRepository.fetchPopular(page).results.toListData()
    }

    override suspend fun createUser(user: User): Flow<UiState<Boolean>> = safeDataCall {
        userRepository.createUser(user).map {
            Log.d("Use Case", "createUser: $it")
            when (it) {
                is SourceResult.Success -> {
                    UiState.Success(it.data)
                }

                is SourceResult.Error -> {
                    UiState.Error(it.throwable)
                }
            }
        }
    }

    override suspend fun signInUser(user: User): LiveData<Boolean> = safeDataCall {
        userRepository.signInUser(user)
    }

    override suspend fun getCurrentUser(): FirebaseUser = safeDataCall {
        userRepository.fetchCurrentUser()
    }

}