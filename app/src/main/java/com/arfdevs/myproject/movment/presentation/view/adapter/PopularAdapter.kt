package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.View
import coil.load
import com.arfdevs.myproject.core.base.BaseListAdapter
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.helper.Constants
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ItemPopularBinding

class PopularAdapter(
    private val onItemClickListener: (PopularModel) -> Unit
): BaseListAdapter<PopularModel, ItemPopularBinding>(ItemPopularBinding::inflate) {
    override fun onItemBind(): (PopularModel, ItemPopularBinding, View, Int) -> Unit = { item, binding, view, _ ->
        with(binding) {
            if (item.posterPath != null) {
                ivMoviePopularBanner.load(Constants.BACKDROP_PATH + item.posterPath)
            } else {
                ivMoviePopularBanner.load(R.drawable.product_thumbnail)
            }
            icRating.setImageResource(R.drawable.ic_star)
            tvMoviePopularTitle.text = item.originalTitle
            tvRating.text = String.format("%.1f", item.voteAverage)

            root.setOnClickListener {
                onItemClickListener(item)
            }
        }
    }

}
