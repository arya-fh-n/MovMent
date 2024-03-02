package com.arfdevs.myproject.movment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arfdevs.myproject.core.domain.model.TokenTopupModel
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import kotlinx.coroutines.runBlocking

class FirebaseViewModel(private val useCase: AppUseCase): ViewModel() {

    private var _tokenAmount = MutableLiveData<Int>()
    val tokenAmount: LiveData<Int> = _tokenAmount

    private var _tokenPrice = MutableLiveData<Int>()
    val tokenPrice: LiveData<Int> = _tokenPrice

    private var _tokenModel = MutableLiveData<TokenTopupModel>()
    var tokenModel: LiveData<TokenTopupModel> = _tokenModel

    fun setTokenModel(tokenModel: TokenTopupModel) {
        _tokenModel.value = tokenModel
    }

    fun setAmount(token: Int) {
        _tokenAmount.value = token
    }

    fun setPrice(price: Int) {
        _tokenPrice.value = price
    }

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

    fun getUID(): String = useCase.getUID()

    fun insertTokenTransaction(transaction: TokenTransactionModel, userId: String) = runBlocking {
        useCase.insertTokenTransaction(transaction, userId)
    }

    fun getTokenBalance(userId: String) = runBlocking {
        useCase.getTokenBalance(userId)
    }

}
