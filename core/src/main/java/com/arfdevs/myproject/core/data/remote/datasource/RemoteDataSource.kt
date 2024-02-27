package com.arfdevs.myproject.core.data.remote.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arfdevs.myproject.core.data.remote.ApiEndpoint
import com.arfdevs.myproject.core.data.remote.responses.PopularResponse
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.helper.SourceResult
import com.arfdevs.myproject.core.helper.safeApiCall
import com.arfdevs.myproject.core.helper.safeDataCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RemoteDataSource(private val endpoint: ApiEndpoint, private val auth: FirebaseAuth) {

    suspend fun fetchPopular(page: Int): PopularResponse {
        return safeApiCall {
            endpoint.fetchPopularMovies(page = page)
        }
    }

    suspend fun createUser(user: User): Flow<SourceResult<Boolean>> = callbackFlow {
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
            Log.d("RemoteDataSource", "createUser: $task")
            trySend(SourceResult.Success(task.isSuccessful))
        }.addOnFailureListener { e ->
            trySend(SourceResult.Error(e))
        }
        awaitClose()

    }

    suspend fun signInUser(user: User): LiveData<Boolean> = safeDataCall {
        val result = MutableLiveData<Boolean>()

        auth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
            result.value = task.isSuccessful
        }

        return@safeDataCall result
    }

    suspend fun fetchCurrentUser(): FirebaseUser = safeApiCall {
        auth.currentUser
    }

}