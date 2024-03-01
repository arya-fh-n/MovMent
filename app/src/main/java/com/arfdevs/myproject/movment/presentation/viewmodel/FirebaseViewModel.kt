package com.arfdevs.myproject.movment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import kotlinx.coroutines.runBlocking

class FirebaseViewModel(private val useCase: AppUseCase): ViewModel() {

    fun getConfigTokenTopupList() = runBlocking {
        useCase.getConfigTokenTopupList()
    }

    fun updateConfigTokenTopupList() = runBlocking {
        useCase.updateConfigPaymentMethodsList()
    }

    fun getConfigPaymentMethodsList() = runBlocking {
        useCase.getConfigPaymentMethodsList()
    }

    fun updateConfigPaymentMethodsList() = runBlocking {
        useCase.updateConfigPaymentMethodsList()
    }

}
