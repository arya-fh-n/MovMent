package com.arfdevs.myproject.movment.presentation.view.ui.cart

import android.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.CartModel
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentCartBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.CartAdapter
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.MovieViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private val viewModel: MovieViewModel by viewModel()

    private val cartAdapter = CartAdapter(
        onItemClickListener = { cart ->
            navigateToDetailFromCart(cart)
        },
        onRemoveItemListener = { cart ->
            context?.let {
                MaterialAlertDialogBuilder(it)
                    .setMessage(getString(R.string.ad_msg_remove_cart))
                    .setNegativeButton(getString(R.string.option_negative)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(getString(R.string.option_positive)) { dialog, _ ->
                        dialog.dismiss()
                        viewModel.deleteCart(cart)
                        context?.let { it1 ->
                            CustomSnackbar.show(
                                it1,
                                binding.root,
                                "Removed from Cart",
                                "Movie is removed from cart."
                            )
                        }
                    }
                    .show()
                    .getButton(AlertDialog.BUTTON_POSITIVE)
                    ?.setTextColor(ContextCompat.getColor(it, R.color.red))
            }
        }
    )

    override fun initView() = with(binding) {
        toolbarCart.title = getString(R.string.toolbar_title_cart)
        tvPaymentTotalTitle.text = getString(R.string.tv_payment_total_title)
        btnBuy.text = getString(R.string.btn_buy_cart)

        rvCart.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = cartAdapter
        }
    }

    override fun initListener() = with(binding) {
        toolbarCart.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        btnBuy.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
        }
    }

    override fun initObserver() = with(viewModel) {
        cartList.observe(viewLifecycleOwner) { list ->
            showError(list.isEmpty())
            cartAdapter.submitList(list)
            var totalPrice = 0
            for (item in list) {
                totalPrice += item?.price ?: 0
            }
            binding.tvPaymentTotal.text = getString(R.string.tv_payment_total, totalPrice)
        }
    }

    private fun fetchData() {
        viewModel.getCartList()
    }

    private fun showError(state: Boolean) = with(binding) {
        rvCart.visible(!state)
        clBottom.visible(!state)
        errorView.visible(state)
        errorView.setMessage(
            getString(R.string.ev_cart_empty_title),
            getString(R.string.ev_cart_empty_desc)
        )
    }

    private fun navigateToDetailFromCart(cart: CartModel) {
        val bundle = bundleOf("movieId" to cart.movieId)
        findNavController().navigate(R.id.action_cartFragment_to_detailFragment, bundle)
    }

}
