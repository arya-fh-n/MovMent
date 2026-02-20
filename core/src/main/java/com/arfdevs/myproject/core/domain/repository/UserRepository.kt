package com.arfdevs.myproject.core.domain.repository

import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.helper.DomainResult
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    suspend fun createUser(user: User): DomainResult<Boolean>
    suspend fun signInUser(user: User): DomainResult<Boolean>
    suspend fun updateUsername(username: String): DomainResult<Boolean>
    suspend fun fetchCurrentUser(): DomainResult<FirebaseUser?>
    suspend fun signOutUser()
    suspend fun getOnboardingState(): Boolean
    suspend fun saveOnboardingState(state: Boolean)
    suspend fun getLanguage(): String
    suspend fun saveLanguage(value: String)
    suspend fun getTheme(): Boolean
    suspend fun saveTheme(value: Boolean)
    suspend fun getUID(): String
    suspend fun saveUID(value: String)

}
