package com.arfdevs.myproject.movment.presentation.view.adapter

import android.util.Log
import android.view.View
import com.arfdevs.myproject.core.base.BaseListAdapter
import com.arfdevs.myproject.core.domain.model.TokenTopupModel
import com.arfdevs.myproject.movment.databinding.ItemTokenTopupBinding

class TokenItemAdapter(
    private val onItemClickListener: (TokenTopupModel) -> Unit,
) : BaseListAdapter<TokenTopupModel, ItemTokenTopupBinding>(ItemTokenTopupBinding::inflate) {

    override fun onItemBind(): (TokenTopupModel, ItemTokenTopupBinding, View, Int) -> Unit = { item, binding, view, position ->
        with(binding) {
            tvTopupAmount.text = item.token.toString()

            root.setOnClickListener {
                onItemClickListener(item)
            }
        }
    }

}
