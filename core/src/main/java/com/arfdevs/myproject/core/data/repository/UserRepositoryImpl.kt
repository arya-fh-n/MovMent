package com.arfdevs.myproject.core.data.repository

import com.arfdevs.myproject.core.data.local.datastore.PreferenceDataStoreHelper
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import com.arfdevs.myproject.core.helper.DomainResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val dataStoreHelper: PreferenceDataStoreHelper,
    private val dispatcher: CoroutinesDispatcherProvider,
) : UserRepository {

    override suspend fun createUser(
        user: User
    ): DomainResult<Boolean> = withContext(dispatcher.io) {
        return@withContext try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()
            DomainResult.Success(true)
        } catch (e: FirebaseAuthException) {
            DomainResult.ErrorState(e.localizedMessage.orEmpty(), -1)
        }
    }

    override suspend fun signInUser(
        user: User
    ): DomainResult<Boolean> = withContext(dispatcher.io) {
        return@withContext try {
            auth.signInWithEmailAndPassword(user.email, user.password).await()
            DomainResult.Success(true)
        } catch (e: FirebaseAuthException) {
            DomainResult.ErrorState(e.localizedMessage.orEmpty(), -1)
        }
    }

    override suspend fun updateUsername(
        username: String
    ): DomainResult<Boolean> = withContext(dispatcher.io) {
        val updates = userProfileChangeRequest {
            displayName = username
        }

        return@withContext try {
            auth.currentUser?.updateProfile(updates)?.await()
            DomainResult.Success(true)
        } catch (e: FirebaseAuthException) {
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
        return@withContext dataStoreHelper.getOnboardingState()
    }

    override suspend fun saveOnboardingState(state: Boolean) = withContext(dispatcher.io) {
        dataStoreHelper.putOnboardingState(state)
    }

    override suspend fun getLanguage(): String = withContext(dispatcher.io) {
        return@withContext dataStoreHelper.getLanguage()
    }

    override suspend fun saveLanguage(value: String) = withContext(dispatcher.io) {
        dataStoreHelper.putLanguage(value)
    }

    override suspend fun getTheme(): Boolean = withContext(dispatcher.io) {
        return@withContext dataStoreHelper.getTheme()
    }

    override suspend fun saveTheme(value: Boolean) = withContext(dispatcher.io) {
        dataStoreHelper.putTheme(value)
    }

    override suspend fun getUID(): String = withContext(dispatcher.io) {
        return@withContext dataStoreHelper.getUID()
    }

    override suspend fun saveUID(value: String) = withContext(dispatcher.io) {
        dataStoreHelper.putUID(value)
    }

}
