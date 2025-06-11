package com.arfdevs.myproject.core.data.repository

import com.arfdevs.myproject.core.data.local.sharedpref.SharedPreferencesHelper
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import com.arfdevs.myproject.core.helper.DomainResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val sharedPrefHelper: SharedPreferencesHelper,
    private val dispatcher: CoroutinesDispatcherProvider,
) : UserRepository {

    override suspend fun createUser(
        user: User
    ): DomainResult<Boolean> = withContext(dispatcher.io) {
        var isSuccess = false
        var e: Exception = Exception()

        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                isSuccess = task.isSuccessful
            }.addOnFailureListener { error ->
                isSuccess = false
                e = error
            }

        return@withContext if (isSuccess) {
            DomainResult.Success(true)
        } else {
            DomainResult.ErrorState(e.localizedMessage.orEmpty(), -1)
        }
    }

    override suspend fun signInUser(
        user: User
    ): DomainResult<Boolean> = withContext(dispatcher.io) {
        var isSuccess = false
        var e: Exception = Exception()

        auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                isSuccess = task.isSuccessful
            }.addOnFailureListener { error ->
                isSuccess = false
                e = error
            }

        return@withContext if (isSuccess) {
            DomainResult.Success(true)
        } else {
            DomainResult.ErrorState(e.localizedMessage.orEmpty(), -1)
        }
    }

    override suspend fun updateUsername(
        username: String
    ): DomainResult<Boolean> = withContext(dispatcher.io) {
        var isSuccess = false
        var e: Exception = Exception()

        val updates = userProfileChangeRequest {
            displayName = username
        }

        auth.currentUser?.updateProfile(updates)?.addOnCompleteListener { task ->
            isSuccess = task.isSuccessful
        }?.addOnFailureListener { error ->
            isSuccess = false
            e = error
        }

        return@withContext if (isSuccess) {
            DomainResult.Success(true)
        } else {
            DomainResult.ErrorState(e.localizedMessage.orEmpty(), -1)
        }
    }

    override suspend fun fetchCurrentUser(): DomainResult<FirebaseUser?> =
        withContext(dispatcher.io) {
            val user = auth.currentUser
            return@withContext if (user == null) {
                DomainResult.ErrorState("No user found.", -2)
            } else {
                DomainResult.Success(auth.currentUser)
            }
        }

    override suspend fun signOutUser() = withContext(dispatcher.io) {
        auth.signOut()
    }

    override suspend fun getOnboardingState(): Boolean = withContext(dispatcher.io) {
        return@withContext sharedPrefHelper.getOnboardingState()
    }

    override suspend fun saveOnboardingState(state: Boolean) = withContext(dispatcher.io) {
        sharedPrefHelper.putOnboardingState(state)
    }

    override suspend fun getLanguage(): String = withContext(dispatcher.io) {
        return@withContext sharedPrefHelper.getLanguage()
    }

    override suspend fun saveLanguage(value: String) = withContext(dispatcher.io) {
        sharedPrefHelper.putLanguage(value)
    }

    override suspend fun getTheme(): Boolean = withContext(dispatcher.io) {
        return@withContext sharedPrefHelper.getTheme()
    }

    override suspend fun saveTheme(value: Boolean) = withContext(dispatcher.io) {
        sharedPrefHelper.putTheme(value)
    }

    override suspend fun getUID(): String = withContext(dispatcher.io) {
        return@withContext sharedPrefHelper.getUID()
    }

    override suspend fun saveUID(value: String) = withContext(dispatcher.io) {
        sharedPrefHelper.putUID(value)
    }

}
