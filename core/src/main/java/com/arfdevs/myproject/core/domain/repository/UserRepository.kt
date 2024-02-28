package com.arfdevs.myproject.core.domain.repository

import com.arfdevs.myproject.core.data.local.datasource.LocalDataSource
import com.arfdevs.myproject.core.data.remote.datasource.RemoteDataSource
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.helper.SourceResult
import com.arfdevs.myproject.core.helper.safeDataCall
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun createUser(user: User): Flow<SourceResult<Boolean>>

    suspend fun signInUser(user: User): Flow<SourceResult<Boolean>>

    suspend fun updateUsername(username: String): Flow<SourceResult<Boolean>>

    suspend fun fetchCurrentUser(): FirebaseUser

    fun getOnboardingState(): Boolean

    fun saveOnboardingState(state: Boolean)

    fun getLanguage(): String

    fun saveLanguage(value: String)

    fun getTheme(): Boolean

    fun saveTheme(value: Boolean)

}

class UserRepositoryImpl(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) :
    UserRepository {

    override suspend fun createUser(user: User): Flow<SourceResult<Boolean>> = safeDataCall {
        remote.createUser(user)
    }

    override suspend fun signInUser(user: User): Flow<SourceResult<Boolean>> = safeDataCall {
        remote.signInUser(user)
    }

    override suspend fun updateUsername(username: String): Flow<SourceResult<Boolean>> =
        safeDataCall {
            remote.updateUsername(username)
        }

    override suspend fun fetchCurrentUser(): FirebaseUser = safeDataCall {
        remote.fetchCurrentUser()
    }

    override fun getOnboardingState(): Boolean = local.getOnboardingState()

    override fun saveOnboardingState(state: Boolean) {
        local.saveOnboardingState(state)
    }

    override fun getLanguage(): String {
        return local.getLanguage()
    }

    override fun saveLanguage(value: String) {
        local.saveLanguage(value)
    }

    override fun getTheme(): Boolean {
        return local.getTheme()
    }

    override fun saveTheme(value: Boolean) {
        local.saveTheme(value)
    }

}