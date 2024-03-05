package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.View
import coil.load
import com.arfdevs.myproject.core.base.BaseListAdapter
import com.arfdevs.myproject.core.domain.model.CheckoutModel
import com.arfdevs.myproject.core.helper.Constants
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ItemCheckoutBinding

class CheckoutAdapter(
    private val onItemClickListener: (CheckoutModel) -> Unit = {}
) : BaseListAdapter<CheckoutModel, ItemCheckoutBinding>(ItemCheckoutBinding::inflate) {

    override fun onItemBind(): (CheckoutModel, ItemCheckoutBinding, View, Int) -> Unit =
        { item, binding, view, _ ->
            with(binding) {
                if (item.posterPath != "") {
                    ivMovieBanner.load(Constants.BACKDROP_PATH + item.posterPath)
                } else {
                    ivMovieBanner.load(R.drawable.product_thumbnail)
                }

                tvItemCartMovieTitle.text = item.originalTitle

                icRating.load(R.drawable.ic_star)
                tvRating.text = String.format("%.1f", item.voteAverage)

                tvPriceTitle.text = view.context.getString(R.string.tv_price_title)
                tvPrice.text = view.context.getString(R.string.tv_movie_price, item.price)

                root.setOnClickListener {
                    onItemClickListener(item)
                }
            }
        }

}
