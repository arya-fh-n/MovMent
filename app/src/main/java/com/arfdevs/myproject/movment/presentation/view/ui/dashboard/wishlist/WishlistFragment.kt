package com.arfdevs.myproject.movment.presentation.view.ui.dashboard.wishlist

import android.graphics.Rect
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentWishlistBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.WishlistAdapter
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.MovieViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.viewModel

class WishlistFragment : BaseFragment<FragmentWishlistBinding>(FragmentWishlistBinding::inflate) {

    private val viewModel: MovieViewModel by viewModel()

    private var wishlistAdapter = WishlistAdapter(
        onItemClickListener = { wishlist ->
            navigateToDetailFromWishlist(wishlist)
        },
        onAddToCartClickListener = { wishlist ->
            context?.let {
                CustomSnackbar.show(
                    it,
                    binding.root,
                    "Added to Cart",
                    "Wishlist item successfully added to cart!"
                )
            }
        },
        onRemoveFavoriteListener = { wishlist ->
            viewModel.deleteWishlist(wishlist)
            context?.let {
                CustomSnackbar.show(
                    it,
                    binding.root,
                    "Favorite Removed",
                    "Movie successfully removed from wishlist!"
                )
            }
        }
    )

    override fun initView() = with(binding) {
        val space = resources.getDimensionPixelSize(R.dimen.list_spacing)

        rvWishlist.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = wishlistAdapter

            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)

                    val itemSize = state.itemCount

                    when {
                        itemSize % 2 == 0 -> {
                            outRect.right = space
                        }
                    }
                    outRect.bottom = space
                }
            })

            setHasFixedSize(true)
        }
    }

    override fun initListener() {}

    override fun initObserver() = with(viewModel) {
        val userId = getUID().hashCode().toString()

        getWishlist(userId).observe(viewLifecycleOwner) { list ->
            showError(list.isEmpty())
            logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundleOf("Open Wishlist" to list))
            wishlistAdapter.submitList(list)
        }
    }

    private fun showError(state: Boolean) = with(binding) {
        rvWishlist.visible(!state)
        errorView.visible(state)
        errorView.setMessage(
            getString(R.string.ev_wishlist_empty_title),
            getString(R.string.ev_wishlist_empty_desc)
        )
    }

    private fun navigateToDetailFromWishlist(wishlist: WishlistModel) {
        val bundle = bundleOf("movieId" to wishlist.movieId)
        activity?.supportFragmentManager?.findFragmentById(R.id.main_navigation_container)
            ?.findNavController()
            ?.navigate(R.id.action_dashboardFragment_to_detailFragment, bundle)
    }

}