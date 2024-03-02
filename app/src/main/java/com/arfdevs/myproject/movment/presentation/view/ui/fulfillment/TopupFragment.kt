package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel
import com.arfdevs.myproject.core.helper.launchAndCollectIn
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentTopupBinding
import com.arfdevs.myproject.movment.presentation.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date
import java.util.Locale

class TopupFragment : BaseFragment<FragmentTopupBinding>(FragmentTopupBinding::inflate),
    PaymentMethodFragment.OnPaymentMethodListener {

    private val viewModel: FirebaseViewModel by viewModel()

    private val safeArgs: TopupFragmentArgs by navArgs()

    private val paymentMethodFragment: PaymentMethodFragment by lazy {
        PaymentMethodFragment()
    }

    private var transactionModel = TokenTransactionModel()
    private var paymentMethod: String = ""
    private var userId: String = ""
    private var currentTIme: String = getCurrentDateInDDMMYYYYFormat()

    override fun initView() = with(binding) {
        btnPay.isEnabled = false

        safeArgs.tokenModel.let { tokenTopup ->
            tvAmount.text = getString(R.string.tv_amount, tokenTopup.token)
            tvPrice.text = getString(R.string.tv_price, tokenTopup.price)
            transactionModel.price = tokenTopup.price
            transactionModel.token = tokenTopup.token
        }

        toolbarTopup.title = getString(R.string.app_name_movment)

        tvTopupPaymentTitle.text = getString(R.string.tv_topup_payment_title)
        tvAmountTitle.text = getString(R.string.tv_amount_title)
        tvPriceTitle.text = getString(R.string.tv_price_title)

        ivPaymentLogo.load(R.drawable.ic_payment_method)
        tvPaymentMethodTitle.text = getString(R.string.tv_payment_method_title)

        btnPay.text = getString(R.string.btn_pay)
    }

    override fun initListener() = with(binding) {
        cardviewTopupPaymentMethod.setOnClickListener {
            paymentMethodFragment.setPaymentMethodListener(this@TopupFragment)
            paymentMethodFragment.show(
                childFragmentManager,
                getString(R.string.tv_payment_method_title)
            )
        }

        toolbarTopup.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        btnPay.setOnClickListener {
            val transaction = transactionModel.copy(
                uid = userId,
                method = paymentMethod,
                date = currentTIme
            )
            collectTransaction(transaction, userId)
        }

    }

    override fun initObserver() = with(viewModel) {
        userId = getUID()
    }

    override fun onPaymentMethodClick(icon: String?, method: String?) {
        with(binding) {
            ivPaymentLogo.load(icon)
            tvPaymentMethodTitle.text = method
            if (method != null) {
                paymentMethod = method
                btnPay.isEnabled = true
            }
        }
    }

    private fun getCurrentDateInDDMMYYYYFormat(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun collectTransaction(
        transaction: TokenTransactionModel,
        userId: String
    ) =
        with(viewModel) {
            insertTokenTransaction(
                transaction,
                userId
            ).launchAndCollectIn(viewLifecycleOwner) { success ->
                if (success) {
                    findNavController().popBackStack()
                }
            }
        }
}