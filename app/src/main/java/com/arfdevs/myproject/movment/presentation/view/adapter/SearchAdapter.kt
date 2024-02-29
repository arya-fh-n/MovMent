package com.arfdevs.myproject.movment.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.helper.Constants
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.ItemSearchBinding

class SearchAdapter(
    private val onItemClickListener: (SearchModel) -> Unit,
    private val onAddToCartClickListener: (SearchModel) -> Unit = {}
) : PagingDataAdapter<SearchModel, SearchAdapter.SearchViewHolder>(DIFF_CALLBACK) {

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchModel) {
            with(binding) {
                ivMovieSearchBanner.load(Constants.BACKDROP_PATH + item.posterPath)
                icRating.load(R.drawable.ic_star)

                tvMovieSearchTitle.text = item.originalTitle
                tvMovieSearchPrice.text =
                    root.context.getString(R.string.tv_movie_price, item.price)
                tvRating.text = String.format("%.1f", item.voteAverage)

                btnAddToCart.text = root.context.getString(R.string.btn_add_to_cart)

                btnAddToCart.setOnClickListener {
                    onAddToCartClickListener.invoke(item)
                }

                root.setOnClickListener {
                    onItemClickListener(item)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchModel>() {

            override fun areItemsTheSame(oldItem: SearchModel, newItem: SearchModel): Boolean {
                return oldItem.id == newItem.id
            }


            override fun areContentsTheSame(oldItem: SearchModel, newItem: SearchModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchItem = getItem(position)
        searchItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

}