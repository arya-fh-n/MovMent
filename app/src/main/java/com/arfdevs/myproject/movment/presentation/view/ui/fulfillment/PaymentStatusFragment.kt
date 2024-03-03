package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentPaymentMethodBinding
import com.arfdevs.myproject.movment.databinding.FragmentPaymentStatusBinding

class PaymentStatusFragment : BaseFragment<FragmentPaymentStatusBinding>(FragmentPaymentStatusBinding::inflate) {

    private val safeArgs: PaymentStatusFragmentArgs by navArgs()

    override fun initView() = with(binding) {
        tvPaymentStatusTitle.text = getString(R.string.tv_payment_status_title)
        tvPaymentStatusDetailTitle.text = getString(R.string.tv_payment_status_detail_title)
        tvPaymentStatusIdTitle.text = getString(R.string.tv_payment_status_transaction_id_title)

        tvPriceTitle.text = getString(R.string.tv_price_status_title)
        tvPaymentDateTitle.text = getString(R.string.tv_payment_date_title)

        btnGoBack.text = getString(R.string.btn_go_back_to_home)

        safeArgs.movieTransactionModel.let { transaction ->
            tvPaymentStatusId.text = transaction.transactionId
            tvPrice.text = transaction.total.toString()
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