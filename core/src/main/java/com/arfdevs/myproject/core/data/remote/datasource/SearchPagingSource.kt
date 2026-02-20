package com.arfdevs.myproject.core.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arfdevs.myproject.core.data.remote.ApiEndpoint
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.helper.Constants.INITIAL_PAGE_INDEX
import com.arfdevs.myproject.core.helper.DataMapper.toSearchList
import com.arfdevs.myproject.core.helper.DomainResult
import com.arfdevs.myproject.core.helper.processResponse

class SearchPagingSource(
    private val api: ApiEndpoint,
    private val query: String
) : PagingSource<Int, SearchModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchModel> {
        val position = params.key ?: INITIAL_PAGE_INDEX
        val result = api.fetchSearch(query = query, page = position)

        val domain = processResponse(result) {
            DomainResult.Success(it.results)
        }

        return when (domain) {
            is DomainResult.Success -> {
                LoadResult.Page(
                    data = domain.data.toSearchList(),
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (domain.data.isEmpty()) null else position + 1
                )
            }

            is DomainResult.EmptyState -> {
                LoadResult.Error(Throwable(message = domain.message))
            }

            is DomainResult.ErrorState -> {
                LoadResult.Error(Throwable(message = domain.message))
            }

            DomainResult.NetworkError -> {
                LoadResult.Error(Throwable(message = "Network error"))
            }

            is DomainResult.TechnicalError -> {
                LoadResult.Error(Throwable(message = "Technical error"))
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchModel>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}