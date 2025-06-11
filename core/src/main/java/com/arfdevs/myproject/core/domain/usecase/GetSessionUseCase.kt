package com.arfdevs.myproject.core.domain.usecase

import com.arfdevs.myproject.core.domain.model.SessionModel
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GetSessionUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) {
    suspend operator fun invoke(): SessionModel = withContext(dispatcher.io) {
        val user = async { userRepository.fetchCurrentUser() }
        val onboardingState = async { userRepository.getOnboardingState() }

        val currentUser = user.await()
        val userOnboardState = onboardingState.await()

        return@withContext SessionModel(
            currentUser?.displayName ?: "",
            currentUser?.uid ?: "",
            userOnboardState
        )
    }
}