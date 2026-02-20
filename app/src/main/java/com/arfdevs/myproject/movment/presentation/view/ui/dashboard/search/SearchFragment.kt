package com.arfdevs.myproject.movment.presentation.view.ui.dashboard.search

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.arfdevs.myproject.core.base.BaseFragment
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.helper.DataMapper.toCartModel
import com.arfdevs.myproject.movment.R
import com.arfdevs.myproject.movment.databinding.FragmentSearchBinding
import com.arfdevs.myproject.movment.presentation.view.adapter.SearchAdapter
import com.arfdevs.myproject.movment.presentation.view.component.CustomSnackbar
import com.arfdevs.myproject.movment.presentation.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: MovieViewModel by viewModel()

    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter(
            onItemClickListener = { search ->
                navigateToDetailFromSearch(search)
            },
            onAddToCartClickListener = { search ->
                viewModel.insertToCart(search.toCartModel())
                context?.let {
                    CustomSnackbar.show(
                        it,
                        binding.root,
                        "Added to Cart",
                        "Item successfully added to cart!"
                    )
                }
            }
        )
    }

    override fun initView() = with(binding) {
        tiSearch.placeholderText = getString(R.string.ti_search_ph)
        btnSearch.text = getString(R.string.ti_search_hint)
        rvSearch.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = searchAdapter
        }
    }

    override fun initListener() {
        with(binding) {
            btnSearch.setOnClickListener {
                viewModel.searchMovies(etSearch.text.toString())
            }
        }
    }

    override fun initObserver() = with(viewModel) {
        movieSearch.observe(viewLifecycleOwner) { pagingData ->
            if (pagingData != null) {
                searchAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
    }

    private fun navigateToDetailFromSearch(search: SearchModel) {
        val bundle = bundleOf("movieId" to search.id)
        activity?.supportFragmentManager?.findFragmentById(R.id.main_navigation_container)
            ?.findNavController()
            ?.navigate(R.id.action_dashboardFragment_to_detailFragment, bundle)
    }

}
