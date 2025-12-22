package com.arfdevs.myproject.core.data.repository

import android.os.Bundle
import com.arfdevs.myproject.core.domain.model.MovieTransactionModel
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel
import com.arfdevs.myproject.core.domain.repository.FirebaseRepository
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import com.arfdevs.myproject.core.helper.DomainResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.ConfigUpdateListenerRegistration
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class FirebaseRepositoryImpl(
    private val config: FirebaseRemoteConfig,
    private val analytics: FirebaseAnalytics,
    private val realtime: DatabaseReference,
    private val dispatcher: CoroutinesDispatcherProvider
) : FirebaseRepository {

    override suspend fun getConfigTokenTopupList(): DomainResult<String> =
        withContext(dispatcher.io) {
            getRemoteString("token_list")
        }

    override suspend fun getConfigPaymentMethodsList(): DomainResult<String> =
        withContext(dispatcher.io) {
            getRemoteString("payment_list")
        }

    override suspend fun updateConfigTokenTopupList() = awaitConfigUpdate("token_list")
    override suspend fun updateConfigPaymentMethodsList() = awaitConfigUpdate("payment_list")

    private suspend fun getRemoteString(key: String): DomainResult<String> {
        return try {
            config.fetchAndActivate().await()
            DomainResult.Success(config.getString(key))
        } catch (e: Exception) {
            DomainResult.ErrorState(e.message ?: "Failed to fetch $key")
        }
    }

    private suspend fun awaitConfigUpdate(targetKey: String): DomainResult<Boolean> =
        withContext(dispatcher.io) {
            suspendCancellableCoroutine { continuation ->
                var registration: ConfigUpdateListenerRegistration? = null

                registration = config.addOnConfigUpdateListener(object : ConfigUpdateListener {
                    override fun onUpdate(configUpdate: ConfigUpdate) {
                        if (configUpdate.updatedKeys.contains(targetKey)) {
                            config.activate().addOnCompleteListener { task ->
                                registration?.remove()
                                if (continuation.isActive) {
                                    continuation.resume(DomainResult.Success(task.isSuccessful))
                                }
                            }
                        }
                    }

                    override fun onError(error: FirebaseRemoteConfigException) {
                        registration?.remove()
                        if (continuation.isActive) {
                            continuation.resume(
                                DomainResult.ErrorState(
                                    error.message ?: "Update error"
                                )
                            )
                        }
                    }
                })

                continuation.invokeOnCancellation { registration.remove() }
            }
        }

    override fun logEvent(eventName: String, bundle: Bundle) {
        analytics.logEvent(eventName, bundle)
    }

    override suspend fun insertTokenTransaction(
        transaction: TokenTransactionModel,
        userId: String
    ): DomainResult<Boolean> = withContext(dispatcher.io) {
        suspendCancellableCoroutine { continuation ->
            realtime.database.reference.child("token_transaction").child(userId).push()
                .setValue(transaction)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(DomainResult.Success(true))
                    } else {
                        continuation.resume(
                            DomainResult.ErrorState(
                                task.exception?.message ?: "Insert failed"
                            )
                        )
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resume(DomainResult.ErrorState(e.message ?: "Insert failed"))
                }
        }
    }

    override suspend fun getTokenBalance(userId: String): DomainResult<Int> =
        withContext(dispatcher.io) {
            try {
                val tokensDeferred = async {
                    realtime.database.reference.child("token_transaction").child(userId).get()
                        .await()
                }
                val moviesDeferred = async {
                    realtime.database.reference.child("movie_transaction").child(userId).get()
                        .await()
                }

                val tokenSnapshot = tokensDeferred.await()
                val movieSnapshot = moviesDeferred.await()

                val currentBalance = tokenSnapshot.children.sumOf {
                    it.child("token").getValue(Int::class.java) ?: 0
                }

                var totalSpent = 0
                movieSnapshot.children.forEach { transactionSnapshot ->
                    val transaction =
                        transactionSnapshot.getValue(MovieTransactionModel::class.java)
                    transaction?.movies?.forEach { movie ->
                        totalSpent += movie?.price ?: 0
                    }
                }

                DomainResult.Success(currentBalance - totalSpent)
            } catch (e: Exception) {
                DomainResult.ErrorState(e.message ?: "An unknown error occurred")
            }
        }

    override suspend fun insertMovieTransaction(
        movieTransaction: MovieTransactionModel,
        userId: String
    ): DomainResult<Boolean> = withContext(dispatcher.io) {
        suspendCancellableCoroutine { continuation ->
            realtime.database.reference.child("movie_transaction").child(userId).push()
                .setValue(movieTransaction)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(DomainResult.Success(true))
                    } else {
                        continuation.resume(
                            DomainResult.ErrorState(
                                task.exception?.message ?: "Insert failed"
                            )
                        )
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resume(DomainResult.ErrorState(e.message ?: "Insert failed"))
                }
        }
    }

    override suspend fun getMovieTransaction(userId: String): DomainResult<List<MovieTransactionModel>> =
        withContext(dispatcher.io) {
            suspendCancellableCoroutine { continuation ->
                realtime.database.reference.child("movie_transaction").child(userId)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        try {
                            val movieTransactions = mutableListOf<MovieTransactionModel>()
                            if (snapshot.exists()) {
                                for (transactionSnapshot in snapshot.children) {
                                    val transaction =
                                        transactionSnapshot.getValue(MovieTransactionModel::class.java)
                                    transaction?.let {
                                        movieTransactions.add(it)
                                    }
                                }
                            }
                            continuation.resume(DomainResult.Success(movieTransactions))
                        } catch (e: Exception) {
                            continuation.resume(
                                DomainResult.ErrorState(
                                    e.message ?: "Failed to parse movie transactions"
                                )
                            )
                        }
                    }
                    .addOnFailureListener { e ->
                        continuation.resume(
                            DomainResult.ErrorState(
                                e.message ?: "Failed to get movie transactions"
                            )
                        )
                    }
            }
        }


}
