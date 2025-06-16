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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseRepositoryImpl(
    private val config: FirebaseRemoteConfig,
    private val analytics: FirebaseAnalytics,
    private val realtime: DatabaseReference,
    private val dispatcher: CoroutinesDispatcherProvider
) : FirebaseRepository {

    override suspend fun getConfigTokenTopupList(): DomainResult<String> =
        withContext(dispatcher.io) {
            suspendCancellableCoroutine { continuation ->
                config.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(DomainResult.Success(config.getString("token_list")))
                    } else {
                        continuation.resume(
                            DomainResult.ErrorState(
                                task.exception?.message ?: "Unknown error"
                            )
                        )
                    }
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
            }
        }

    override suspend fun updateConfigTokenTopupList(): DomainResult<Boolean> =
        withContext(dispatcher.io) {
            suspendCancellableCoroutine { continuation ->
                config.addOnConfigUpdateListener(object : ConfigUpdateListener {
                    override fun onUpdate(configUpdate: ConfigUpdate) {
                        if (configUpdate.updatedKeys.contains("token_list")) {
                            config.activate().addOnCompleteListener { task ->
                                continuation.resume(DomainResult.Success(task.isSuccessful))
                            }.addOnFailureListener { e ->
                                continuation.resume(
                                    DomainResult.ErrorState(
                                        e.message ?: "Activation failed"
                                    )
                                )
                            }
                        } else {
                            continuation.resume(DomainResult.Success(false))
                        }
                    }

                    override fun onError(error: FirebaseRemoteConfigException) {
                        continuation.resume(
                            DomainResult.ErrorState(
                                error.message ?: "Config update error"
                            )
                        )
                    }
                })
            }
        }

    override suspend fun getConfigPaymentMethodsList(): DomainResult<String> =
        withContext(dispatcher.io) {
            suspendCancellableCoroutine { continuation ->
                config.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(DomainResult.Success(config.getString("payment_list")))
                    } else {
                        continuation.resume(
                            DomainResult.ErrorState(
                                task.exception?.message ?: "Unknown error"
                            )
                        )
                    }
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
            }
        }

    override suspend fun updateConfigPaymentMethodsList(): DomainResult<Boolean> =
        withContext(dispatcher.io) {
            suspendCancellableCoroutine { continuation ->
                config.addOnConfigUpdateListener(object : ConfigUpdateListener {
                    override fun onUpdate(configUpdate: ConfigUpdate) {
                        if (configUpdate.updatedKeys.contains("payment_list")) {
                            config.activate().addOnCompleteListener { task ->
                                continuation.resume(DomainResult.Success(task.isSuccessful))
                            }.addOnFailureListener { e ->
                                continuation.resume(
                                    DomainResult.ErrorState(
                                        e.message ?: "Activation failed"
                                    )
                                )
                            }
                        } else {
                            continuation.resume(DomainResult.Success(false))
                        }
                    }

                    override fun onError(error: FirebaseRemoteConfigException) {
                        continuation.resume(
                            DomainResult.ErrorState(
                                error.message ?: "Config update error"
                            )
                        )
                    }
                })
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
            suspendCancellableCoroutine { continuation ->
                realtime.database.reference.child("token_transaction").child(userId)
                    .get()
                    .addOnSuccessListener { tokenSnapshot ->
                        var currentBalance = 0
                        for (transactions in tokenSnapshot.children) {
                            val token = transactions.child("token").getValue(Int::class.java)
                            if (token != null) {
                                currentBalance += token
                            }
                        }

                        // Get movie transactions
                        realtime.database.reference.child("movie_transaction").child(userId)
                            .get()
                            .addOnSuccessListener { movieSnapshot ->
                                var totalSpent = 0
                                if (movieSnapshot.exists()) {
                                    for (transactionSnapshot in movieSnapshot.children) {
                                        val transaction =
                                            transactionSnapshot.getValue(MovieTransactionModel::class.java)
                                        transaction?.movies?.forEach { movie ->
                                            totalSpent += movie?.price ?: 0
                                        }
                                    }
                                }
                                val finalBalance = currentBalance - totalSpent
                                continuation.resume(DomainResult.Success(finalBalance))
                            }
                            .addOnFailureListener { e ->
                                continuation.resume(
                                    DomainResult.ErrorState(
                                        e.message ?: "Failed to get movie transactions"
                                    )
                                )
                            }
                    }
                    .addOnFailureListener { e ->
                        continuation.resume(
                            DomainResult.ErrorState(
                                e.message ?: "Failed to get token transactions"
                            )
                        )
                    }
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
