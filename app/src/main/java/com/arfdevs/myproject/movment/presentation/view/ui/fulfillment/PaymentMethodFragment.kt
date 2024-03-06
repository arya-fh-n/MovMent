package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arfdevs.myproject.core.domain.model.PaymentTypeModel
import com.arfdevs.myproject.core.helper.launchAndCollectIn
import com.arfdevs.myproject.movment.databinding.FragmentPaymentMethodBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.PaymentTypeAdapter
import com.arfdevs.myproject.movment.presentation.viewmodel.FirebaseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentMethodFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPaymentMethodBinding

    private var paymentMethodListener: OnPaymentMethodListener? = null

    private val paymentList: MutableList<PaymentTypeModel> = mutableListOf()

    private var paymentTypeAdapter = PaymentTypeAdapter(paymentList,
        onPaymentMethodClickListener = {
            paymentMethodListener?.onPaymentMethodClick(it.image, it.label)
            dismiss()
        })

    private val viewModel: FirebaseViewModel by viewModel()

    interface OnPaymentMethodListener {
        fun onPaymentMethodClick(
            icon: String?,
            method: String?
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentMethodBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    private fun initObserver() {
        getPaymentMethods()
        updatePaymentMethods()
    }

    private fun initView() = with(binding) {
        rvPaymentType.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = paymentTypeAdapter
        }
    }

    fun setPaymentMethodListener(listener: OnPaymentMethodListener) {
        paymentMethodListener = listener
    }

    private fun getPaymentMethods() = with(viewModel) {
        getConfigPaymentMethodsList().launchAndCollectIn(viewLifecycleOwner) {
            if (paymentList.isEmpty()) {
                paymentList.addAll(it)
                paymentTypeAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun updatePaymentMethods() = with(viewModel) {
        updateConfigPaymentMethodsList().launchAndCollectIn(viewLifecycleOwner) { success ->
            if (success) {
                getPaymentMethods()
            }
        }
    }

}
