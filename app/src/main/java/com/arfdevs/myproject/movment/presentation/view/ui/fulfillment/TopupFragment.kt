package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentTopupBinding

class TopupFragment : BaseFragment<FragmentTopupBinding>(FragmentTopupBinding::inflate),
    PaymentMethodFragment.OnPaymentMethodListener {

    private val safeArgs: TopupFragmentArgs by navArgs()

    private val paymentMethodFragment: PaymentMethodFragment by lazy {
        PaymentMethodFragment()
    }

    override fun initView() = with(binding) {
        safeArgs.tokenModel.let { tokenTopup ->
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

        }

    }

    override fun initObserver() {

    }

    override fun onPaymentMethodClick(icon: String?, method: String?) {
        with(binding) {
            ivPaymentLogo.load(icon)
            tvPaymentMethodTitle.text = method
        }
    }

}