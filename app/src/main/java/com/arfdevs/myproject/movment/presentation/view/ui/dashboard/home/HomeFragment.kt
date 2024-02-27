package com.arfdevs.myproject.movment.presentation.view.ui.dashboard.home

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentHomeBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.NowPlayingAdapter
import com.arfdevs.myproject.movment.presentation.view.adapter.PopularAdapter
import com.arfdevs.myproject.movment.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var rvPopular: RecyclerView
    private lateinit var rvNowPlaying: RecyclerView


    private var popularAdapter = PopularAdapter()
    private var nowPlayingAdapter = NowPlayingAdapter()

    override fun initView() = with(binding) {
        tvUsername.text = getString(R.string.tv_username_ph, "Username")
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

}