package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentTopupStatusBinding

class TopupStatusFragment :
    BaseFragment<FragmentTopupStatusBinding>(FragmentTopupStatusBinding::inflate) {

    private val safeArgs: TopupStatusFragmentArgs by navArgs()

    override fun initView() = with(binding) {
        tvTopupStatusTitle.text = getString(R.string.tv_topup_status_title)
        tvPriceTitle.text = getString(R.string.tv_price_status_title)
        tvPaymentMethodTitle.text = getString(R.string.tv_payment_method_status_title)
        tvPaymentDateTitle.text = getString(R.string.tv_payment_date_title)

        btnGoBack.text = getString(R.string.btn_go_back_to_home)

        safeArgs.tokenTransactionModel.let { transaction ->
            tvAmount.text = getString(R.string.tv_token_status, transaction.token)
            tvPrice.text = getString(R.string.tv_price, transaction.price)
            tvPaymentMethodName.text = transaction.method
            tvPaymentDate.text = transaction.date
        }
    }

    override fun initListener() = with(binding) {
        btnGoBack.setOnClickListener {
            activity?.supportFragmentManager?.findFragmentById(R.id.main_navigation_container)
                ?.findNavController()
                ?.popBackStack()
        }
    }

    override fun initObserver() {}

}
