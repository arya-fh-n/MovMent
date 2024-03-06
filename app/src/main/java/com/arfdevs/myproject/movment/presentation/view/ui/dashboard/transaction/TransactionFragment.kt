package com.arfdevs.myproject.movment.presentation.view.ui.dashboard.transaction

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.helper.launchAndCollectIn
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentTransactionBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.TransactionAdapter
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionFragment :
    BaseFragment<FragmentTransactionBinding>(FragmentTransactionBinding::inflate) {

    private val viewModel: MovieViewModel by viewModel()

    private var userId = ""

    private val transactionAdapter = TransactionAdapter(
        onItemClickListener = {
            context?.let { it1 ->
                CustomSnackbar.show(
                    it1,
                    binding.root,
                    getString(R.string.csb_transaction_detail_title),
                    getString(R.string.csb_transaction_detail_msg)
                )
            }
        }
    )

    override fun initView() = with(binding) {
        rvTransaction.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = transactionAdapter
            setHasFixedSize(true)
        }
    }

    override fun initListener() {}

    override fun initObserver() {
        with(viewModel) {
            userId = getUID()

            getTransactionHistory(userId).launchAndCollectIn(viewLifecycleOwner) { list ->
                showError(list.isEmpty())
                transactionAdapter.submitList(list)
            }
        }
    }

    private fun showError(state: Boolean) = with(binding) {
        rvTransaction.visible(!state)
        errorView.visible(state)
        errorView.setMessage(
            getString(R.string.ev_transaction_empty_title),
            getString(R.string.ev_transaction_empty_desc)
        )
    }

}
