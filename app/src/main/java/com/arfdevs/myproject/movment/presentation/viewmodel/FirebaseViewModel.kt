package com.arfdevs.myproject.movment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arfdevs.myproject.core.domain.model.TokenTopupModel
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel
import com.arfdevs.myproject.core.domain.repository.FirebaseRepository
import com.arfdevs.myproject.core.domain.repository.MovieRepository
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import kotlinx.coroutines.runBlocking

class FirebaseViewModel(
    private val movieRepo: MovieRepository,
    private val userRepo: UserRepository,
    private val firebaseRepo: FirebaseRepository,
    private val dispatcher: CoroutinesDispatcherProvider
): ViewModel() {

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
        useCase.updateConfigTokenTopupList()
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
