package com.arfdevs.myproject.movment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfdevs.myproject.core.data.remote.responses.PaymentMethodsResponse
import com.arfdevs.myproject.core.data.remote.responses.TokenTopupResponse
import com.arfdevs.myproject.core.domain.model.PaymentTypeModel
import com.arfdevs.myproject.core.domain.model.TokenTopupModel
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel
import com.arfdevs.myproject.core.domain.repository.FirebaseRepository
import com.arfdevs.myproject.core.domain.repository.UserRepository
import com.arfdevs.myproject.core.helper.CoroutinesDispatcherProvider
import com.arfdevs.myproject.core.helper.DataMapper.toUIData
import com.arfdevs.myproject.core.helper.DomainResult
import com.arfdevs.myproject.core.helper.UiState
import com.google.gson.Gson
import kotlinx.coroutines.launch

class FirebaseViewModel(
    private val userRepo: UserRepository,
    private val firebaseRepo: FirebaseRepository,
    private val dispatcher: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _tokenAmount = MutableLiveData<Int>()
    val tokenAmount: LiveData<Int> = _tokenAmount

    private val _tokenPrice = MutableLiveData<Int>()
    val tokenPrice: LiveData<Int> = _tokenPrice

    private val _tokenModel = MutableLiveData<TokenTopupModel>()
    val tokenModel: LiveData<TokenTopupModel> = _tokenModel

    private val _tokenTopupList = MutableLiveData<UiState<List<TokenTopupModel>>>()
    val tokenTopupList: LiveData<UiState<List<TokenTopupModel>>> = _tokenTopupList

    private val _updateTokenTopupState = MutableLiveData<UiState<Boolean>>()
    val updateTokenTopupState: LiveData<UiState<Boolean>> = _updateTokenTopupState

    private val _paymentMethodList = MutableLiveData<UiState<List<PaymentTypeModel>>>()
    val paymentMethodList: LiveData<UiState<List<PaymentTypeModel>>> = _paymentMethodList

    private val _updatePaymentMethodState = MutableLiveData<UiState<Boolean>>()
    val updatePaymentMethodState: LiveData<UiState<Boolean>> = _updatePaymentMethodState

    private val _tokenBalance = MutableLiveData<UiState<Int>>()
    val tokenBalance: LiveData<UiState<Int>> = _tokenBalance

    private val _insertTokenTransactionState = MutableLiveData<UiState<Boolean>>()
    val insertTokenTransactionState: LiveData<UiState<Boolean>> = _insertTokenTransactionState

    var tokenTransactionModel = TokenTransactionModel()
        private set

    var paymentMethod: String = ""

    fun setTokenModel(tokenModel: TokenTopupModel) {
        _tokenModel.value = tokenModel
    }

    fun setAmount(token: Int) {
        _tokenAmount.value = token
    }

    fun setPrice(price: Int) {
        _tokenPrice.value = price
    }

    fun getConfigTokenTopupList() = viewModelScope.launch(dispatcher.io) {
        val state = when (val result = firebaseRepo.getConfigTokenTopupList()) {
            is DomainResult.Success -> {
                try {
                    val json = result.data.length
                    if (json == 0) {
                        val parsedData =
                            Gson().fromJson(result.data, TokenTopupResponse::class.java)
                        val mappedTokenTopupItem = parsedData.map {
                            it.toUIData()
                        }
                        UiState.Success(mappedTokenTopupItem)
                    } else {
                        UiState.Error("Data is null")
                    }
                } catch (e: Exception) {
                    UiState.Error(e.message ?: "Unknown error")
                }
            }

            else -> {
                UiState.Error("Something went wrong. Please try again later.")
            }
        }

        _tokenTopupList.postValue(state)
    }

    fun updateConfigTokenTopupList() = viewModelScope.launch(dispatcher.io) {
        val state = when (val result = firebaseRepo.updateConfigTokenTopupList()) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error("Something went wrong. Please try again later.")
        }
        _updateTokenTopupState.postValue(state)
    }

    fun getConfigPaymentMethodsList() = viewModelScope.launch(dispatcher.io) {
        val state = when (val result = firebaseRepo.getConfigPaymentMethodsList()) {
            is DomainResult.Success -> {
                try {
                    val json = result.data.length
                    if (json == 0) {
                        val parsedData =
                            Gson().fromJson(result.data, PaymentMethodsResponse::class.java)
                        val mappedTokenTopupItem = parsedData.data.map {
                            it.toUIData()
                        }
                        UiState.Success(mappedTokenTopupItem)
                    } else {
                        UiState.Error("Data is null")
                    }
                } catch (e: Exception) {
                    UiState.Error(e.message ?: "Unknown error")
                }
            }

            else -> {
                UiState.Error("Something went wrong. Please try again later.")
            }
        }

        _paymentMethodList.postValue(state)
    }

    fun updateConfigPaymentMethodsList() = viewModelScope.launch(dispatcher.io) {
        val state = when (val result = firebaseRepo.updateConfigPaymentMethodsList()) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error("Something went wrong. Please try again later.")
        }
        _updatePaymentMethodState.postValue(state)
    }

    fun insertTokenTransaction(
        transaction: TokenTransactionModel
    ) = viewModelScope.launch(dispatcher.io) {
        this@FirebaseViewModel.tokenTransactionModel = transaction
        val userId = userRepo.getUID()
        val model = transaction.copy(method = this@FirebaseViewModel.paymentMethod)
        val state = when (val result = firebaseRepo.insertTokenTransaction(model, userId)) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error("Something went wrong. Please try again later.")
        }
        _insertTokenTransactionState.postValue(state)
    }

    fun setSelectedPaymentMethod(
        method: String
    ) {
        paymentMethod = method
    }

    fun getTokenBalance() = viewModelScope.launch(dispatcher.io) {
        val userId = userRepo.getUID()
        val state = when (val result = firebaseRepo.getTokenBalance(userId)) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error("Something went wrong. Please try again later.")
        }
        _tokenBalance.postValue(state)
    }

}
