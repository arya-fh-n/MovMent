package com.arfdevs.myproject.movment.presentation.view.ui.fulfillment

import android.app.AlertDialog
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.CheckoutModel
import com.arfdevs.myproject.core.domain.model.MovieTransactionModel
import com.arfdevs.myproject.core.helper.DataMapper.toCheckoutModelList
import com.arfdevs.myproject.core.helper.DataMapper.toMovieTransactionModel
import com.arfdevs.myproject.core.helper.launchAndCollectIn
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentCheckoutBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.convertTotransactionID
import com.arfdevs.myproject.movment.presentation.helper.Constants.getCurrentDateInDDMMYYYYFormat
import com.arfdevs.myproject.movment.presentation.view.adapter.CheckoutAdapter
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.MovieViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutFragment : BaseFragment<FragmentCheckoutBinding>(FragmentCheckoutBinding::inflate) {

    private val viewModel: MovieViewModel by viewModel()

    private var transactionModel = MovieTransactionModel()

    private val checkoutAdapter = CheckoutAdapter(
        onItemClickListener = { checkout ->
            navigateToDetailFromCheckout(checkout)
        }
    )

    override fun initView() = with(binding) {
        toolbarCheckout.title = getString(R.string.toolbar_title_checkout)
        tvPaymentTotalTitle.text = getString(R.string.tv_payment_total_title)
        btnCheckout.text = getString(R.string.btn_checkout)

        tvBalanceIs.text = getString(R.string.tv_token_balance_is)
        tvBalance.text = getString(R.string.tv_token_balance, 0)
        ivBalance.load(R.drawable.ic_balance)


        rvCheckout.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = checkoutAdapter
        }
    }

    override fun initListener() = with(binding) {
        toolbarCheckout.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        btnToTokenBalance.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_tokenFragment)
        }

        val userId = viewModel.getUID()

        btnCheckout.setOnClickListener {
            context?.let {
                MaterialAlertDialogBuilder(it)
                    .setMessage(getString(R.string.ad_msg_checkout))
                    .setNegativeButton(getString(R.string.option_negative)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(getString(R.string.option_positive)) { dialog, _ ->
                        dialog.dismiss()
                        collectCheckout(transactionModel, userId)
                        CustomSnackbar.show(
                            it,
                            binding.root,
                            "Checkout Click",
                            "Movie is successfully bought!"
                        )
                        viewModel.removeAllCartItem()
                    }
                    .show()
                    .getButton(AlertDialog.BUTTON_NEGATIVE)
                    ?.setTextColor(ContextCompat.getColor(it, R.color.red))
            }
        }
    }

    override fun initObserver() = with(viewModel) {
        val userIdHash = getUID()

        getCartList(userIdHash).observe(viewLifecycleOwner) { list ->
            val checkoutList = list.toCheckoutModelList()
            checkoutAdapter.submitList(checkoutList)
            var totalPrice = 0
            for (item in list) {
                totalPrice += item?.price ?: 0
            }
            binding.tvPaymentTotal.text = getString(R.string.tv_payment_total, totalPrice)

            val userIdClean = getUID()

            getTokenBalance(userIdClean).launchAndCollectIn(viewLifecycleOwner) { balance ->
                with(binding) {
                    tvBalance.text = getString(R.string.tv_balance, balance)
                    btnCheckout.isEnabled = balance >= totalPrice

                    val date = getCurrentDateInDDMMYYYYFormat()
                    val transactionId = date.convertTotransactionID()

                    this@CheckoutFragment.transactionModel = checkoutList.toMovieTransactionModel(
                        uid = userIdClean,
                        total = totalPrice,
                        date = date
                    ).copy(transactionId = transactionId)
                }
            }
        }
    }

    private fun navigateToDetailFromCheckout(checkout: CheckoutModel) {
        val bundle = bundleOf("movieId" to checkout.movieId)
        findNavController().navigate(R.id.action_checkoutFragment_to_detailFragment, bundle)
    }

    private fun collectCheckout(transactionModel: MovieTransactionModel, userId: String) =
        with(viewModel) {
            insertTransactionModel(
                transactionModel,
                userId
            ).launchAndCollectIn(viewLifecycleOwner) { success ->
                Log.d("Fragment", "collectCheckout: $success")
                if (success) {
                    findNavController().navigate(R.id.action_checkoutFragment_to_paymentStatusFragment, bundleOf("movieTransactionModel" to transactionModel))
                }
            }
        }

}