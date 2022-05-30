package com.umbat.storyappsubmission.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.umbat.storyappsubmission.api.ApiService
import com.umbat.storyappsubmission.model.StoryResponseItem

class StoryPagingSource(private val apiService: ApiService, private val token: String) :
    PagingSource<Int, StoryResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponseItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoriesList(
                token = token,
                position,
                params.loadSize
            )
            val data = responseData.listStory

            LoadResult.Page(
                data = data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (data.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}