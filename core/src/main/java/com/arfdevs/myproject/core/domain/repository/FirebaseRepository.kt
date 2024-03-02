package com.arfdevs.myproject.core.domain.repository

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
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

}

class FirebaseRepositoryImpl(
    private val config: FirebaseRemoteConfig,
    private val analytics: FirebaseAnalytics
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

}