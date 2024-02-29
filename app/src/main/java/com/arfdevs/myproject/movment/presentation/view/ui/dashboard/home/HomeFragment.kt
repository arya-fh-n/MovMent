package com.arfdevs.myproject.movment.presentation.view.ui.dashboard.home

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.NowPlayingModel
import com.arfdevs.myproject.core.domain.model.PopularModel
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentHomeBinding
import com.arfdevs.myproject.movment.presentation.helper.Constants.USERNAME
import com.arfdevs.myproject.movment.presentation.view.adapter.NowPlayingAdapter
import com.arfdevs.myproject.movment.presentation.view.adapter.PopularAdapter
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var rvPopular: RecyclerView
    private lateinit var rvNowPlaying: RecyclerView


    private var popularAdapter = PopularAdapter(
        onItemClickListener = { popular ->
            navigateToDetailFromPopular(popular)
        }
    )

    private var nowPlayingAdapter = NowPlayingAdapter(
        onItemClickListener = { nowPlaying ->
            navigateToDetailFromNowPlaying(nowPlaying)
        },
        onAddToCartClickListener = {
            context?.let { it1 ->
                CustomSnackbar.show(
                    it1,
                    binding.root,
                    "Add to cart clicked!",
                    "Movie is added to cart"
                )
            }
        }
    )

    override fun initView() = with(binding) {
        tvUsername.text = getString(R.string.tv_username_ph, USERNAME)
        tvBalanceIs.text = getString(R.string.tv_balance_is)
        ivBalance.load(R.drawable.ic_balance)
        tvBalance.text = getString(R.string.tv_balance)

        tvSectionPopular.text = getString(R.string.tv_section_popular)
        tvSectionNowPlaying.text = getString(R.string.tv_section_now_playing)

        this@HomeFragment.rvPopular = rvPopular
        this@HomeFragment.rvPopular.run {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = popularAdapter
        }

        this@HomeFragment.rvNowPlaying = rvNowPlaying
        this@HomeFragment.rvNowPlaying.run {
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            adapter = nowPlayingAdapter
        }

        viewModel.getPopularMovies(1)
        viewModel.getNowPlaying(1)
        viewModel.getCurrentUser()
    }

    override fun initListener() {

    }

    override fun initObserver() {
        with(viewModel) {
            currentUser.observe(viewLifecycleOwner) { user ->
                saveUID(user.uid)

                with(binding) {
                    tvUsername.text = getString(R.string.tv_username_ph, user.displayName)
                }
            }

            responseNowPlaying.observe(viewLifecycleOwner) { list ->
                nowPlayingAdapter.submitList(list)
            }

            responsePopular.observe(viewLifecycleOwner) { list ->
                popularAdapter.submitList(list)
            }

        }
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