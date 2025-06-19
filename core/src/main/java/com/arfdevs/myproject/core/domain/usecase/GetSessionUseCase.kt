package com.arfdevs.myproject.core.domain.usecase

import com.arfdevs.myproject.core.domain.model.SessionModel
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import com.arfdevs.myproject.core.helper.DomainResult
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GetSessionUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) {
    suspend operator fun invoke(): SessionModel = withContext(dispatcher.io) {
        val userDeferred = async { userRepository.fetchCurrentUser() }
        val onboardingStateDeferred = async { userRepository.getOnboardingState() }

        val user = userDeferred.await()
        val onboardingState = onboardingStateDeferred.await()

        val displayName: String
        val uid: String

        when (user) {
            is DomainResult.Success -> {
                val firebaseUser = user.data
                displayName = firebaseUser?.displayName ?: ""
                uid = firebaseUser?.uid ?: ""
            }

            else -> {
                displayName = ""
                uid = ""
            }
        }

        return@withContext SessionModel(
            displayName,
            uid,
            onboardingState
        )
    }
}
