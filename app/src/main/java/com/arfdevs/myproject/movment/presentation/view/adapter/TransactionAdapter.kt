package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.View
import coil.load
import com.arfdevs.myproject.core.base.BaseListAdapter
import com.arfdevs.myproject.core.domain.model.MovieTransactionModel
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ItemTransactionBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.convertToDateInDDMMYYYYFormat

class TransactionAdapter(
    private val onItemClickListener: (MovieTransactionModel) -> Unit = {}
) : BaseListAdapter<MovieTransactionModel, ItemTransactionBinding>(ItemTransactionBinding::inflate) {

    override fun onItemBind(): (MovieTransactionModel, ItemTransactionBinding, View, Int) -> Unit =
        { item, binding, view, _ ->
            with(binding) {
                ivItemIcon.load(R.drawable.ic_movie)
                tvItemTransactionId.text =
                    view.context.getString(R.string.tv_item_transaction_id, item.transactionId)

                tvItemTransactionDate.text = item.date.convertToDateInDDMMYYYYFormat()

                item.movies.first()?.let {
                    ivMovieBanner.load(it.posterPath)
                    tvItemFirstMovieTitle.text = it.originalTitle
                }

                tvItemTotalMovies.text =
                    view.context.getString(R.string.tv_item_transaction_total, item.movies.count())
                tvItemTotalTitle.text = view.context.getString(R.string.tv_payment_total_title)
                tvItemTotal.text = view.context.getString(R.string.tv_payment_total, item.total)

                root.setOnClickListener {
                    onItemClickListener(item)
                }
            }
        }

}
