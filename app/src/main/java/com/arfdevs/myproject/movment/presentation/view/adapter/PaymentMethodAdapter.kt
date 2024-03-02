package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arfdevs.myproject.core.domain.model.PaymentMethodModel
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ItemPaymentMethodBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.VIEW_ALPHA

class PaymentMethodAdapter(
    private val methodItem: List<PaymentMethodModel>,
    private val onItemClickListener: (PaymentMethodModel) -> Unit
) : RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    inner class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemPaymentMethodBinding = ItemPaymentMethodBinding.bind(itemView)

        fun bindMethod(model: PaymentMethodModel) {
            with(binding) {
                val ovoVisibility = model.label.equals("OVO", true)

                val gopayVisibility = model.label.equals("GoPay", true)

                if (ovoVisibility || gopayVisibility) {
                    sivMethodIcon.visible(true)
                    ivMethodIcon.visible(true)
                    sivMethodIcon.load(model.image)
                } else {
                    sivMethodIcon.isVisible = false
                    ivMethodIcon.isVisible = true
                    ivMethodIcon.load(model.image)
                }

                tvMethodName.text = model.label

                if (!model.status) {
                    with(clPaymentItem) {
                        isEnabled = false
                        isClickable = false
                        alpha = VIEW_ALPHA
                    }
                }

                itemView.setOnClickListener {
                    onItemClickListener.invoke(model)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentMethodAdapter.PaymentMethodViewHolder {
        val binding = ItemPaymentMethodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PaymentMethodViewHolder(binding.root)
    }

    override fun getItemCount(): Int = methodItem.size

    override fun onBindViewHolder(
        holder: PaymentMethodAdapter.PaymentMethodViewHolder,
        position: Int
    ) {
        val currentItem = methodItem[position]
        holder.bindMethod(currentItem)
    }

}