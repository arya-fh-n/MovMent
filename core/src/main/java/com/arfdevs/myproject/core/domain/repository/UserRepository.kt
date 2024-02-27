package com.arfdevs.myproject.core.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.arfdevs.myproject.core.data.remote.datasource.RemoteDataSource
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.helper.SourceResult
import com.arfdevs.myproject.core.helper.safeDataCall
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun createUser(user: User): Flow<SourceResult<Boolean>>

    suspend fun signInUser(user: User): LiveData<Boolean>

    suspend fun fetchCurrentUser(): FirebaseUser

}

class UserRepositoryImpl(private val remote: RemoteDataSource) : UserRepository {

    override suspend fun createUser(user: User): Flow<SourceResult<Boolean>> = safeDataCall {
        val state = remote.createUser(user)
        Log.d("Repository", "createUser: $state")
        state
    }

    override suspend fun signInUser(user: User): LiveData<Boolean> = safeDataCall {
        remote.signInUser(user)
    }

    override suspend fun fetchCurrentUser(): FirebaseUser = safeDataCall {
        remote.fetchCurrentUser()
    }

}