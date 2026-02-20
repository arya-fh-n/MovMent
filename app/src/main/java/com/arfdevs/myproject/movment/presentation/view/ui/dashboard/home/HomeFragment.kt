package com.arfdevs.myproject.movment.presentation.view.ui.dashboard.home

import android.os.Handler
import android.os.Looper
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.core.helper.DataMapper.toCartModel
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentHomeBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.HOME_TOKEN_FETCH_DELAY
import com.arfdevs.myproject.movment.presentation.helper.Constants.USERNAME
import com.arfdevs.myproject.movment.presentation.view.adapter.NowPlayingAdapter
import com.arfdevs.myproject.movment.presentation.view.adapter.PopularAdapter
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()

    private val popularAdapter = PopularAdapter(
        onItemClickListener = { popular ->
            navigateToDetailFromPopular(popular)
        }
    )

    private val nowPlayingAdapter = NowPlayingAdapter(
        onItemClickListener = { nowPlaying ->
            navigateToDetailFromNowPlaying(nowPlaying)
        },
        onAddToCartClickListener = { movie ->
            viewModel.insertToCart(movie.toCartModel())
            context?.let { it1 ->
                CustomSnackbar.show(
                    it1,
                    binding.root,
                    "Added to Cart",
                    "Movie is added to cart!"
                )
            }
        }
    )

    override fun initView() = with(binding) {
        tvUsername.text = getString(R.string.tv_username_ph, USERNAME)
        tvBalanceIs.text = getString(R.string.tv_balance_is)
        ivBalance.setImageResource(R.drawable.ic_balance)

        tvSectionPopular.text = getString(R.string.tv_section_popular)
        tvSectionNowPlaying.text = getString(R.string.tv_section_now_playing)

        setupHome()

        rvPopular.run {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = popularAdapter
        }

        rvNowPlaying.run {
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            adapter = nowPlayingAdapter
        }

    }

    override fun initListener() = with(binding) {
        btnToTokenBalance.setOnClickListener {
            activity?.supportFragmentManager?.findFragmentById(R.id.main_navigation_container)
                ?.findNavController()
                ?.navigate(R.id.action_dashboardFragment_to_tokenFragment)
        }
    }

    override fun initObserver() = with(viewModel) {
        currentUser.observe(viewLifecycleOwner) { user ->
            saveUID(user?.uid.orEmpty())
            binding.tvUsername.text =
                getString(R.string.tv_username_ph, user?.displayName ?: "undefined")
        }

        nowPlayingList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> nowPlayingAdapter.submitList(state.data)
                else -> {}
            }
        }

        logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundleOf("Home shown" to "HomeFragment"))

        popularList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> popularAdapter.submitList(state.data)
                else -> {}
            }
        }

        tokenBalance.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    binding.tvBalance.text = getString(R.string.tv_balance, state.data)
                }

                else -> {}
            }
        }
    }

    private fun setupHome() = with(viewModel) {
        getPopularMovies(1)
        getNowPlaying(1)
        getCurrentUser()

        Handler(Looper.getMainLooper()).postDelayed({
            getTokenBalance()
        }, HOME_TOKEN_FETCH_DELAY)
    }

    private fun navigateToDetailFromPopular(popular: PopularModel) {
        val bundle = bundleOf("movieId" to popular.id)
        activity?.supportFragmentManager?.findFragmentById(R.id.main_navigation_container)
            ?.findNavController()
            ?.navigate(R.id.action_dashboardFragment_to_detailFragment, bundle)
    }

    private fun navigateToDetailFromNowPlaying(nowPlaying: NowPlayingModel) {
        val bundle = bundleOf("movieId" to nowPlaying.id)
        activity?.supportFragmentManager?.findFragmentById(R.id.main_navigation_container)
            ?.findNavController()
            ?.navigate(R.id.action_dashboardFragment_to_detailFragment, bundle)
    }

}
