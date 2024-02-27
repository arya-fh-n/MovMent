package com.arfdevs.myproject.core.domain.usecase

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

    suspend fun signInUser(user: User): Flow<UiState<Boolean>>

    suspend fun updateUsername(username: String): Flow<UiState<Boolean>>

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
            when (it) {
                is SourceResult.Success -> {
                    UiState.Success(it.data)
                }

                is SourceResult.Error -> {
                    UiState.Error(it.throwable)
                }

                is SourceResult.Loading -> {
                    UiState.Loading
                }
            }
        }
    }

    override suspend fun signInUser(user: User): Flow<UiState<Boolean>> = safeDataCall {
        userRepository.signInUser(user).map {
            when (it) {
                is SourceResult.Success -> {
                    UiState.Success(it.data)
                }

                is SourceResult.Error -> {
                    UiState.Error(it.throwable)
                }

                is SourceResult.Loading -> {
                    UiState.Loading
                }
            }
        }
    }

    override suspend fun updateUsername(username: String): Flow<UiState<Boolean>> = safeDataCall {
        userRepository.updateUsername(username).map {
            when (it) {
                is SourceResult.Success -> {
                    UiState.Success(it.data)
                }

                is SourceResult.Error -> {
                    UiState.Error(it.throwable)
                }

                is SourceResult.Loading -> {
                    UiState.Loading
                }
            }
        }
    }

    override suspend fun getCurrentUser(): FirebaseUser = safeDataCall {
        userRepository.fetchCurrentUser()
    }

}