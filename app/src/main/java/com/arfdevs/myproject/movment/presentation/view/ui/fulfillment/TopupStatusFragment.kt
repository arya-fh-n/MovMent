package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentTopupStatusBinding
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar

class TopupStatusFragment : BaseFragment<FragmentTopupStatusBinding>(FragmentTopupStatusBinding::inflate) {

    override fun initView() = with(binding) {
        tvTopupStatusTitle.text = getString(R.string.tv_topup_status_title)
        tvAmount.text = getString(R.string.tv_token_ph_2)
        tvPriceTitle.text = getString(R.string.tv_price_status_title)
        tvPrice.text = getString(R.string.tv_price_ph)
        tvPaymentMethodTitle.text = getString(R.string.tv_payment_method_title)
        tvPaymentMethodName.text = getString(R.string.tv_payment_method_name)
        tvPaymentDateTitle.text = getString(R.string.tv_payment_date_title)
        tvPaymentDate.text = getString(R.string.tv_payment_date_ph)
    }

    override fun initListener() = with(binding) {
        btnGoBack.setOnClickListener {
            context?.let { it1 ->
                CustomSnackbar.show(
                    it1,
                    root,
                    "Go Back To Home",
                    "Topup is successful!"
                )
            }
        }
    }

    override fun initObserver() {}

}