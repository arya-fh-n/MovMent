package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.View
import coil.load
import com.arfdevs.myproject.core.base.BaseListAdapter
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.helper.Constants
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ItemWishlistBinding

class WishlistAdapter(
    private val onItemClickListener: (WishlistModel) -> Unit,
    private val onAddToCartClickListener: (WishlistModel) -> Unit = {},
    private val onRemoveFavoriteListener: (WishlistModel) -> Unit = {}
) : BaseListAdapter<WishlistModel, ItemWishlistBinding>(ItemWishlistBinding::inflate) {

    override fun onItemBind(): (WishlistModel, ItemWishlistBinding, View, Int) -> Unit =
        { item, binding, view, _ ->
            with(binding) {
                ivMovieWishlistBanner.load(Constants.BACKDROP_PATH + item.posterPath)
                tvMovieWishlistTitle.text = item.originalTitle
                tvMovieWishlistPrice.text = view.context.getString(R.string.tv_movie_price, item.price)
                icRating.load(R.drawable.ic_star)
                tvRating.text = String.format("%.1f", item.voteAverage)

                btnAddToCart.text = view.context.getString(R.string.btn_add_to_cart)

                btnAddToCart.setOnClickListener {
                    onAddToCartClickListener.invoke(item)
                }

                btnRemoveWishlist.setOnClickListener {
                    onRemoveFavoriteListener.invoke(item)
                }

                root.setOnClickListener {
                    onItemClickListener(item)
                }
            }

        }

}