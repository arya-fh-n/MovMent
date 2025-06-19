package com.arfdevs.myproject.movment.presentation.view.ui.dashboard.token

import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.TokenTopupModel
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.core.helper.enabled
import com.arfdevs.myproject.core.helper.isError
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentTokenBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.TOKEN_PRICE_PER_RUPIAH
import com.arfdevs.myproject.movment.presentation.view.adapter.TokenItemAdapter
import com.arfdevs.myproject.movment.presentation.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TokenFragment : BaseFragment<FragmentTokenBinding>(FragmentTokenBinding::inflate) {

    private val viewModel: FirebaseViewModel by viewModel()

    private val tokenItemAdapter = TokenItemAdapter(
        onItemClickListener = {
            binding.etTopupAmount.setText(it.token.toString())
        }
    )

    private var amount: Int = 0
    private var price: Int = 0
    private var tokenModel = TokenTopupModel()

    override fun initView() = with(binding) {
        btnContinue.enabled(false)
        toolbarToken.title = getString(R.string.app_name_movment)
        tvBalanceIs.text = getString(R.string.tv_token_balance_is)
        tvBalance.text = getString(R.string.tv_token_balance, 0)

        tiTopupAmount.hint = getString(R.string.tv_token_manual_amt)
        btnContinue.text = getString(R.string.btn_token_continue)

        ivBalance.load(R.drawable.ic_balance)
        fetchData()
        rvTopupAmountSelection.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = tokenItemAdapter
        }
    }

    override fun initListener() = with(binding) {
        toolbarToken.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        etTopupAmount.doAfterTextChanged {
            btnContinue.isEnabled = etTopupAmount.text?.isNotEmpty() ?: false
            if (it != null) {
                val amount = etTopupAmount.text.toString().toIntOrNull()
                amount?.let { token ->
                    viewModel.setAmount(token)
                    viewModel.setPrice(token.times(TOKEN_PRICE_PER_RUPIAH))
                }
            }
        }

        btnContinue.setOnClickListener {
            viewModel.setTokenModel(
                TokenTopupModel(
                    token = amount,
                    price = price
                )
            )
            navigateToTopup(tokenModel)
        }
    }

    override fun initObserver() = with(viewModel) {
        tokenAmount.observe(viewLifecycleOwner) {
            amount = it
        }

        tokenPrice.observe(viewLifecycleOwner) {
            price = it
        }

        tokenModel.observe(viewLifecycleOwner) {
            this@TokenFragment.tokenModel = it
        }

        tokenTopupList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> tokenItemAdapter.submitList(state.data)
                else -> showError(state.isError(true))
            }
        }

        updateTokenTopupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data) {
                        getConfigTokenTopupList()
                    }
                }

                else -> showError(state.isError(true))
            }
        }

        tokenBalance.observe(viewLifecycleOwner) { balance ->
            when (balance) {
                is UiState.Success -> {
                    binding.tvBalance.text = getString(R.string.tv_balance, balance.data)
                }

                else -> showError(balance.isError(true))
            }
        }
    }

    private fun fetchData() = with(viewModel) {
        getConfigTokenTopupList()
        updateConfigTokenTopupList()
        getTokenBalance()
    }

    private fun showError(state: Boolean) = with(binding) {
        rvTopupAmountSelection.visible(!state)
        errorView.visible(state)
        errorView.setMessage(
            "Loading Failed",
            "Token topup amount cannot be fetched."
        ) {
            viewModel.getConfigTokenTopupList()
        }
    }

    private fun navigateToTopup(topup: TokenTopupModel) {
        val bundle = bundleOf("tokenModel" to topup)
        findNavController().navigate(R.id.action_tokenFragment_to_topupFragment, bundle)
    }

}
