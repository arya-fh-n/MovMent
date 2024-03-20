package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.View
import coil.load
import com.arfdevs.myproject.core.base.BaseListAdapter
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.helper.enabled
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ItemWishlistBinding

class WishlistAdapter(
    private val onItemSet: (WishlistModel) -> Unit,
    private val onItemClickListener: (WishlistModel) -> Unit,
    private val onAddToCartClickListener: (WishlistModel) -> Unit = {},
    private val onRemoveFavoriteListener: (WishlistModel) -> Unit = {}
) : BaseListAdapter<WishlistModel, ItemWishlistBinding>(ItemWishlistBinding::inflate) {

    private var isInCart = false

    override fun onItemBind(): (WishlistModel, ItemWishlistBinding, View, Int) -> Unit =
        { wishlist, binding, view, _ ->
            with(binding) {
                onItemSet(wishlist)

                val item = wishlist.copy(isInCart = isInCart)
                if (item.posterPath != "") {
                    ivMovieWishlistBanner.load(item.posterPath)
                } else {
                    ivMovieWishlistBanner.load(R.drawable.product_thumbnail)
                }

                tvMovieWishlistTitle.text = item.originalTitle
                tvMovieWishlistPrice.text =
                    view.context.getString(R.string.tv_movie_price, item.price)
                icRating.setImageResource(R.drawable.ic_star)
                tvRating.text = String.format("%.1f", item.voteAverage)

                btnAddToCart.text = view.context.getString(R.string.btn_add_to_cart)

                when (item.isInCart) {
                    false -> {
                        btnAddToCart.enabled(true)
                    }

                    else -> {
                        btnAddToCart.enabled(false)
                    }
                }

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

    fun itemIsInCart(state: Boolean) {
        isInCart = state
    }

}
