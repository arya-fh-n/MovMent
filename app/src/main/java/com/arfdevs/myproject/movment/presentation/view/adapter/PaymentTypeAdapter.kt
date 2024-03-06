package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arfdevs.myproject.core.domain.model.PaymentMethodModel
import com.arfdevs.myproject.core.domain.model.PaymentTypeModel
import com.arfdevs.myproject.movment.databinding.ItemPaymentTypeBinding

class PaymentTypeAdapter(
    private val list: List<PaymentTypeModel>,
    private val onPaymentMethodClickListener: (PaymentMethodModel) -> Unit
) : RecyclerView.Adapter<PaymentTypeAdapter.PaymentViewHolder>() {
    inner class PaymentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding: ItemPaymentTypeBinding = ItemPaymentTypeBinding.bind(itemView)

        fun bindCategory(model: PaymentTypeModel) {
            with(binding) {
                tvTitle.text = model.title
                rvPaymentMethod.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = PaymentMethodAdapter(model.item, onPaymentMethodClickListener)
                    setHasFixedSize(true)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentTypeAdapter.PaymentViewHolder {
        val binding = ItemPaymentTypeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PaymentViewHolder(binding.root)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PaymentTypeAdapter.PaymentViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bindCategory(currentItem)
    }

}
