package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arfdevs.myproject.core.domain.model.PaymentTypeModel
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.movment.databinding.FragmentPaymentMethodBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.PaymentTypeAdapter
import com.arfdevs.myproject.movment.presentation.viewmodel.FirebaseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentMethodFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPaymentMethodBinding

    private var paymentMethodListener: OnPaymentMethodListener? = null

    private val paymentList: MutableList<PaymentTypeModel> = mutableListOf()

    private var paymentTypeAdapter = PaymentTypeAdapter(
        paymentList,
        onPaymentMethodClickListener = {
            paymentMethodListener?.onPaymentMethodClick(it.image, it.label)
            dismiss()
        })

    private val viewModel: FirebaseViewModel by viewModel()

    interface OnPaymentMethodListener {
        fun onPaymentMethodClick(
            icon: String,
            method: String
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
        fetchData()
        initObserver()
    }

    private fun fetchData() = with(viewModel) {
        getConfigPaymentMethodsList()
        updateConfigPaymentMethodsList()
    }

    private fun initObserver() = with(viewModel) {
        updatePaymentMethodState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Success -> {
                    getConfigPaymentMethodsList()
                }

                else -> {}
            }
        }

        paymentMethodList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (paymentList.isEmpty()) {
                        paymentList.addAll(state.data)
                        paymentTypeAdapter.notifyDataSetChanged()
                    }
                }

                else -> {}
            }

        }
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

}
