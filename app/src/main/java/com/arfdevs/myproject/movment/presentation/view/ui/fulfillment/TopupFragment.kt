package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.TokenTransactionModel
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.core.helper.enabled
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentTopupBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.getCurrentDateInDDMMYYYYFormat
import com.arfdevs.myproject.movment.presentation.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopupFragment : BaseFragment<FragmentTopupBinding>(FragmentTopupBinding::inflate),
    PaymentMethodFragment.OnPaymentMethodListener {

    private val viewModel: FirebaseViewModel by viewModel()

    private val safeArgs: TopupFragmentArgs by navArgs()

    private val paymentMethodFragment: PaymentMethodFragment by lazy {
        PaymentMethodFragment()
    }

    private val tokenModel by lazy {
        safeArgs.tokenModel
    }

    override fun initView() = with(binding) {
        btnPay.enabled(false)

        tokenModel.let { tokenTopup ->
            tvAmount.text = getString(R.string.tv_amount, tokenTopup.token)
            tvPrice.text = getString(R.string.tv_price, tokenTopup.price)
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
            val transaction = TokenTransactionModel(
                token = tokenModel.token,
                price = tokenModel.price,
                date = getCurrentDateInDDMMYYYYFormat()
            )
            viewModel.insertTokenTransaction(transaction)
        }
    }

    override fun initObserver() = with(viewModel) {
        insertTokenTransactionState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data) {
                        findNavController().navigate(
                            R.id.action_topupFragment_to_topupStatusFragment,
                            bundleOf("tokenTransactionModel" to tokenTransactionModel)
                        )
                    }
                }

                else -> {}
            }
        }
    }

    override fun onPaymentMethodClick(icon: String, method: String) {
        with(binding) {
            ivPaymentLogo.load(icon)
            tvPaymentMethodTitle.text = method
            viewModel.setSelectedPaymentMethod(method)
            btnPay.enabled(true)
        }
    }

}
