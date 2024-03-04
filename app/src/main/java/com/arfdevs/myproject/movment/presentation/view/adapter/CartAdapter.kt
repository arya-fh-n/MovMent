package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.View
import coil.load
import com.arfdevs.myproject.core.base.BaseListAdapter
import com.arfdevs.myproject.core.domain.model.CartModel
import com.arfdevs.myproject.core.helper.Constants
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ItemCartBinding

class CartAdapter(
    private val onItemClickListener: (CartModel) -> Unit = {},
    private val onRemoveItemListener: (CartModel) -> Unit = {}
) : BaseListAdapter<CartModel, ItemCartBinding>(ItemCartBinding::inflate) {

    override fun onItemBind(): (CartModel, ItemCartBinding, View, Int) -> Unit = { item, binding, view, _ ->
        with(binding) {
            ivMovieBanner.load(Constants.BACKDROP_PATH + item.posterPath)

            tvItemCartMovieTitle.text = item.originalTitle

            icRating.load(R.drawable.ic_star)
            tvRating.text = String.format("%.1f", item.voteAverage)

            tvPriceTitle.text = view.context.getString(R.string.tv_price_title)
            tvPrice.text = view.context.getString(R.string.tv_movie_price, item.price)

            btnRemoveWishlist.setOnClickListener {
                onRemoveItemListener(item)
            }

            root.setOnClickListener {
                onItemClickListener(item)
            }
        }
    }

}
