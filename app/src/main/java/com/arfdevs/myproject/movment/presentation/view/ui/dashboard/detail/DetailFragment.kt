package com.arfdevs.myproject.movment.presentation.view.ui.dashboard.detail

import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.WishlistModel
import com.arfdevs.myproject.core.helper.Constants
import com.arfdevs.myproject.core.helper.Constants.USER_ID
import com.arfdevs.myproject.core.helper.onError
import com.arfdevs.myproject.core.helper.onLoading
import com.arfdevs.myproject.core.helper.onSuccess
import com.arfdevs.myproject.core.helper.visible
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentDetailBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.ERROR
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val viewModel: MovieViewModel by viewModel()

    private val safeArgs: DetailFragmentArgs by navArgs()

    override fun initView() = with(binding) {
        safeArgs.movieId.let { id ->
            viewModel.getMovieDetails(id)
            viewModel.checkFavorite(id)
        }

        toolbarDetail.title = getString(R.string.app_name)

        ivMovieBackdrop.load(R.drawable.product_thumbnail)
        tvMovieDetailTitle.text = getString(R.string.tv_movie_detail_title)
        tvMovieDetailGenres.text = getString(R.string.tv_movie_detail_genres)

        fabFavorite.load(R.drawable.ic_favorite_outline)

        tvMovieDetailRatingsTitle.text = getString(R.string.tv_movie_detail_ratings_title)
        rbRating.rating = 0.0f
        tvMovieDetailPrice.text = getString(R.string.tv_movie_detail_price, 0)

        tvMovieDetailTagline.text = getString(R.string.tv_movie_detail_tagline)
        tvMovieDetailOverview.text = getString(R.string.tv_movie_detail_overview)

        btnRent.text = getString(R.string.btn_rent_directly)
        btnAddToCart.text = getString(R.string.btn_add_to_cart)

    }

    override fun initListener() = with(binding) {
        viewModel.isFavorite.observe(this@DetailFragment) { isFavorite ->
            Log.d("Fragment", "initListener: isFavorite: $isFavorite")

            if (isFavorite == 1) {
                fabFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                fabFavorite.setImageResource(R.drawable.ic_favorite_outline)
            }

            fabFavorite.setOnClickListener {
                if (isFavorite == 1) {
                    viewModel.deleteWishlistFromDetail()
                    fabFavorite.setImageResource(R.drawable.ic_favorite_outline)

                    context?.let {
                        CustomSnackbar.show(
                            it,
                            root,
                            "Movie Removed from Wishlist",
                            "Movie is removed from wishlist!"
                        )
                    }
                } else {
                    viewModel.insertWishlistMovie()
                    fabFavorite.setImageResource(R.drawable.ic_favorite)

                    context?.let {
                        CustomSnackbar.show(
                            it,
                            root,
                            "Movie Favorited",
                            "Movie is added to wishlist!"
                        )
                    }
                }
            }
        }

        toolbarDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        btnRent.setOnClickListener {
            context?.let { it1 ->
                CustomSnackbar.show(
                    it1,
                    root,
                    "Rent clicked",
                    "Movie is rented"
                )
            }
        }

        btnAddToCart.setOnClickListener {
            context?.let { it1 ->
                CustomSnackbar.show(
                    it1,
                    root,
                    "Add to cart clicked",
                    "Movie is added to cart"
                )
            }
        }
    }

    override fun initObserver() = with(viewModel) {
        responseDetails.observe(viewLifecycleOwner) { state ->
            binding.apply {
                state.onSuccess { detail ->
                    loadingOverlay.visible(false)
                    loadingAnim.visible(false)

                    if (detail.backdropPath?.isNotEmpty() == true) {
                        ivMovieBackdrop.load(Constants.BACKDROP_PATH + detail.backdropPath)
                    } else {
                        ivMovieBackdrop.load(R.drawable.product_thumbnail)
                    }

                    tvMovieDetailTitle.text = detail.originalTitle
                    tvMovieDetailGenres.text = detail.genres.joinToString(separator = " / ")

                    setWishlistModel(
                        WishlistModel(
                            movieId = detail.id,
                            userId = USER_ID,
                            originalTitle = detail.originalTitle,
                            posterPath = Constants.BACKDROP_PATH + detail.posterPath,
                            voteAverage = detail.voteAverage,
                            price = detail.price
                        )
                    )

                    tvMovieDetailRatingsTitle.text =
                        getString(R.string.tv_movie_detail_ratings_title)
                    rbRating.rating = detail.voteAverage.toFloat()
                    tvMovieDetailPrice.text =
                        getString(R.string.tv_movie_detail_price, detail.price)

                    if (detail.tagline.isNotEmpty()) {
                        tvMovieDetailTagline.text =
                            getString(R.string.tv_movie_detail_tagline, detail.tagline)
                    } else {
                        tvMovieDetailTagline.visible(false)
                    }

                    tvMovieDetailOverview.text = detail.overview
                }.onError { e ->
                    loadingOverlay.visible(false)
                    loadingAnim.visible(false)
                    context?.let {
                        CustomSnackbar.show(
                            it, binding.root,
                            getString(R.string.err_title_login_failed),
                            e.localizedMessage?.toString() ?: ERROR
                        )
                    }
                }.onLoading {
                    loadingOverlay.visible(true)
                    loadingAnim.visible(true)
                }
            }
        }
    }

}