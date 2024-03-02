package com.arfdevs.myproject.movment.presentation.view.ui.dashboard.token

import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.TokenTopupModel
import com.arfdevs.myproject.core.helper.launchAndCollectIn
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentTokenBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.TokenItemAdapter
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TokenFragment : BaseFragment<FragmentTokenBinding>(FragmentTokenBinding::inflate) {

    private val tokenItemAdapter = TokenItemAdapter(
        onItemClickListener = {
            binding.etTopupAmount.setText(it.token.toString())
        }
    )

    private val viewModel: FirebaseViewModel by viewModel()

    private var amount: Int = 0
    private var price: Int = 0
    private var tokenModel = TokenTopupModel()

    override fun initView() = with(binding) {
        btnContinue.isEnabled = false
        val token = 10
        toolbarToken.title = getString(R.string.app_name_movment)
        tvBalanceIs.text = getString(R.string.tv_token_balance_is)
        tvBalance.text = getString(R.string.tv_token_balance, token)

        tiTopupAmount.hint = getString(R.string.tv_token_manual_amt)
        btnContinue.text = getString(R.string.btn_token_continue)

        ivBalance.load(R.drawable.ic_balance)

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
                    viewModel.setPrice(token.times(150))
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

    override fun initObserver() {
        getConfig()
        updateConfig()

        observeAmount()
        observePrice()
        observeTokenModel()
    }

    private fun getConfig() = with(viewModel) {
        getConfigTokenTopupList().launchAndCollectIn(viewLifecycleOwner) { topupList ->
            tokenItemAdapter.submitList(topupList)
        }
    }

    private fun updateConfig() = with(viewModel) {
        updateConfigTokenTopupList().launchAndCollectIn(viewLifecycleOwner) { success ->
            showError(!success)
            if (success) {
                getConfig()
            }
        }
    }

    private fun showError(state: Boolean) = with(binding) {
        rvTopupAmountSelection.visible(!state)
        errorView.visible(state)
        errorView.setMessage(
            "Loading Failed",
            "Token topup amount cannot be fetched."
        ) {
            getConfig()
        }
    }

    private fun observeTokenModel() = with(viewModel) {
        tokenModel.observe(viewLifecycleOwner) {
            this@TokenFragment.tokenModel = it
        }
    }

    private fun observeAmount() = with(viewModel) {
        tokenAmount.observe(viewLifecycleOwner) {
            amount = it
        }
    }

    private fun observePrice() = with(viewModel) {
        tokenPrice.observe(viewLifecycleOwner) {
            price = it
        }
    }

    private fun navigateToTopup(topup: TokenTopupModel) {
        val bundle = bundleOf("tokenModel" to topup)
        findNavController().navigate(R.id.action_tokenFragment_to_topupFragment, bundle)
    }

}