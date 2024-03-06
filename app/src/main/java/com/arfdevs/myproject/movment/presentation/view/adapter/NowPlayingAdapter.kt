package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.View
import coil.load
import com.arfdevs.myproject.core.base.BaseListAdapter
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.helper.Constants
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ItemNowPlayingBinding

class NowPlayingAdapter(
    private val onItemClickListener: (NowPlayingModel) -> Unit,
    private val onAddToCartClickListener: (NowPlayingModel) -> Unit = {}
) : BaseListAdapter<NowPlayingModel, ItemNowPlayingBinding>(ItemNowPlayingBinding::inflate) {

    override fun onItemBind(): (NowPlayingModel, ItemNowPlayingBinding, View, Int) -> Unit =
        { item, binding, view, _ ->
            with(binding) {
                if (item.posterPath != null) {
                    ivMovieNowPlayingBanner.load(Constants.BACKDROP_PATH + item.posterPath)
                } else {
                    ivMovieNowPlayingBanner.load(R.drawable.product_thumbnail)
                }
                icRating.setImageResource(R.drawable.ic_star)
                tvMovieNowPlayingTitle.text = item.originalTitle
                tvMovieNowPlayingPrice.text =
                    view.context.getString(R.string.tv_movie_price, item.price)
                tvRating.text = String.format("%.1f", item.voteAverage)

                btnAddToCart.text = view.context.getString(R.string.btn_add_to_cart)

                btnAddToCart.setOnClickListener {
                    onAddToCartClickListener.invoke(item)
                }

                root.setOnClickListener {
                    onItemClickListener(item)
                }
            }
        }

}
