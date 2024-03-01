package com.arfdevs.myproject.core.data.remote.datasource

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.arfdevs.myproject.core.data.remote.ApiEndpoint
import com.arfdevs.myproject.core.data.remote.responses.MovieDetailsResponse
import com.arfdevs.myproject.core.data.remote.responses.NowPlayingResponse
import com.arfdevs.myproject.core.data.remote.responses.PopularResponse
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.helper.SourceResult
import com.arfdevs.myproject.core.helper.safeApiCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RemoteDataSource(private val endpoint: ApiEndpoint, private val auth: FirebaseAuth) {

    suspend fun fetchPopular(page: Int): PopularResponse {
        return safeApiCall {
            endpoint.fetchPopularMovies(page = page)
        }
    }

    suspend fun fetchNowPlaying(page: Int): NowPlayingResponse {
        return safeApiCall {
            endpoint.fetchNowPlayingMovies(page = page)
        }
    }

    suspend fun fetchMovieDetails(movieId: Int): MovieDetailsResponse {
        return safeApiCall {
            endpoint.fetchMovieDetails(movieId)
        }
    }

    suspend fun fetchSearch(query: String): LiveData<PagingData<SearchModel>> = safeApiCall {
        Pager(
            config = PagingConfig(
                enablePlaceholders = true,
                pageSize = 20,
                initialLoadSize = 10,
                prefetchDistance = 1
            ),
            pagingSourceFactory = {
                SearchPagingSource(apiEndpoint = endpoint, query = query)
            }
        ).liveData
    }

    suspend fun createUser(user: User): Flow<SourceResult<Boolean>> = callbackFlow {
        trySend(SourceResult.Loading(false))

        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                trySend(SourceResult.Success(task.isSuccessful))
            }.addOnFailureListener { e ->
                trySend(SourceResult.Error(e))
            }

        awaitClose()
    }

    suspend fun signInUser(user: User): Flow<SourceResult<Boolean>> = callbackFlow {
        trySend(SourceResult.Loading(false))

        auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                trySend(SourceResult.Success(task.isSuccessful))
            }.addOnFailureListener { e ->
                trySend(SourceResult.Error(e))
            }

        awaitClose()
    }

    suspend fun updateUsername(username: String): Flow<SourceResult<Boolean>> = callbackFlow {
        trySend(SourceResult.Loading(false))

        val updates = userProfileChangeRequest {
            displayName = username
        }

        auth.currentUser?.updateProfile(updates)?.addOnCompleteListener { task ->
            trySend(SourceResult.Success(task.isSuccessful))
        }?.addOnFailureListener { e ->
            trySend(SourceResult.Error(e))
        }

        awaitClose()
    }

    suspend fun signOutUser() = safeApiCall {
        auth.signOut()
    }

    suspend fun fetchCurrentUser(): FirebaseUser? = safeApiCall {
        auth.currentUser
    }

}