package com.arfdevs.myproject.core.domain.repository

import android.os.Bundle
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface FirebaseRepository {

    fun getConfigTokenTopupList(): Flow<String>

    fun updateConfigTokenTopupList(): Flow<Boolean>

    fun getConfigPaymentMethodsList(): Flow<String>

    fun updateConfigPaymentMethodsList(): Flow<Boolean>

    fun logEvent(eventName: String, bundle: Bundle)

    fun insertTokenTransaction(
        transaction: TokenTransactionModel,
        userId: String
    ): Flow<Boolean>

    fun getTokenBalance(userId: String): Flow<Int>

}

class FirebaseRepositoryImpl(
    private val config: FirebaseRemoteConfig,
    private val analytics: FirebaseAnalytics,
    private val realtime: DatabaseReference
) : FirebaseRepository {

    override fun getConfigTokenTopupList(): Flow<String> = callbackFlow {
        config.fetchAndActivate().addOnCompleteListener {
            trySend(config.getString("token_list"))
        }.addOnFailureListener { e ->
            trySend(e.message.toString())
        }

        awaitClose()
    }

    override fun updateConfigTokenTopupList(): Flow<Boolean> = callbackFlow {
        config.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("token_list")) {
                    config.activate().addOnCompleteListener { task ->
                        trySend(task.isSuccessful)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                trySend(error.message?.isNotEmpty() ?: false)
            }

        })

        awaitClose()
    }

    override fun getConfigPaymentMethodsList(): Flow<String> = callbackFlow {
        config.fetchAndActivate().addOnCompleteListener {
            trySend(config.getString("payment_list"))
        }.addOnFailureListener { e ->
            trySend(e.message.toString())
        }

        awaitClose()
    }

    override fun updateConfigPaymentMethodsList(): Flow<Boolean> = callbackFlow {
        config.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("payment_list")) {
                    config.activate().addOnCompleteListener { task ->
                        trySend(task.isSuccessful)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                trySend(error.message?.isNotEmpty() ?: false)
            }

        })

        awaitClose()
    }

    override fun logEvent(eventName: String, bundle: Bundle) {
        analytics.logEvent(eventName, bundle)
    }

    override fun insertTokenTransaction(
        transaction: TokenTransactionModel,
        userId: String
    ): Flow<Boolean> =
        callbackFlow {
            trySend(false)

            realtime.database.reference.child("token_transaction").child(userId).push()
                .setValue(transaction)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful)
                }.addOnFailureListener { e ->
                    trySend(e.message!!.isNotEmpty())
                }

            awaitClose()
        }

    override fun getTokenBalance(userId: String): Flow<Int> = callbackFlow {
        trySend(0)

        realtime.database.reference.child("token_transaction").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var currentBalance = 0

                    for (transactions in snapshot.children) {
                        val token = transactions.child("token").getValue(Int::class.java)
                        if (token != null) {
                            currentBalance += token
                        }
                    }
                    trySend(currentBalance)
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(0)
                }

            })

        awaitClose()
    }

}