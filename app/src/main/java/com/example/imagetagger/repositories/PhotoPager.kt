package com.example.imagetagger.repositories

import android.content.ContentResolver
import android.content.Context
import android.util.Log
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagetagger.models.FileEntry
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class PhotosPagingSource(
    private val repository: PhotoRepository,
) : PagingSource<Int, FileEntry>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FileEntry> {
        Log.d("Pager", "Pager trying to load page ${params.key}")

        return try {
            val page = params.key ?: 0 // Start at page 0
            val pageSize = params.loadSize // Use the requested load size
            val response = repository.getImages(params.loadSize, page * pageSize)
            val nextKey = if (response.size < pageSize) null else page + 1
            val prevKey = if (page == 0) null else page - 1
            Log.d("Pager", "Pager got ${response.size} items back from the repo")
            LoadResult.Page(
                data = response,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FileEntry>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            Log.i("Anchor", "anchor position is $anchorPosition")
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?: 0
        }
    }
}
