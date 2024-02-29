package com.arfdevs.myproject.core.helper

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <T> safeApiCall(
    dispatcher: CoroutineContext = Dispatchers.IO,
    apiCall: suspend () -> T
): T {
    return withContext(dispatcher) {
        try {
            apiCall()
        } catch (error: Throwable) {
            throw error
        }
    }
}

fun <T> safeApiCallFlow(
    dispatcher: CoroutineContext = Dispatchers.IO,
    apiCall: suspend () -> T?
): Flow<T> {
    return flow {
        withContext(dispatcher) {
            try {
                val data = apiCall.invoke() ?: throw Exception("Network no")
                emit(data)
            } catch (e: Exception) {
                throw e
            }
        }
    }.flowOn(dispatcher)
}

suspend fun <T> MutableStateFlow<UiState<T>>.asMutableStateFlow(
    dataCall: suspend () -> T
) {
    this.update { UiState.Loading }
    try {
        val data = dataCall.invoke()
        this.update { UiState.Success(data) }
    } catch (e: Throwable) {
        this.update { UiState.Error(e) }
    }
}

suspend fun <T> safeDataCall(
    dispatcher: CoroutineContext = Dispatchers.Default,
    dataCall: suspend () -> T
): T {
    return withContext(dispatcher) {
        try {
            dataCall()
        } catch (e: Exception) {
            throw e
        }
    }
}

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}