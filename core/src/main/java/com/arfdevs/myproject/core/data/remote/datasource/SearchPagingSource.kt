package com.arfdevs.myproject.core.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arfdevs.myproject.core.data.remote.ApiEndpoint
import com.arfdevs.myproject.core.domain.model.SearchModel
import com.arfdevs.myproject.core.helper.Constants.INITIAL_PAGE_INDEX
import com.arfdevs.myproject.core.helper.DataMapper.toSearchList

class SearchPagingSource(
    private val apiEndpoint: ApiEndpoint,
    private val query: String
) : PagingSource<Int, SearchModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchModel> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiEndpoint.fetchSearch(query = query, page = position)

            val movies = response.results.toSearchList()

            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchModel>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}