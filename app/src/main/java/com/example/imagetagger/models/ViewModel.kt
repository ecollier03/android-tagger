package com.example.imagetagger.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.imagetagger.repositories.PhotoRepository
import com.example.imagetagger.repositories.PhotosPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _files = MutableStateFlow<List<FileEntry>>(emptyList())
    val photoQuery: StateFlow<List<FileEntry>> = _files.asStateFlow()
    val allTags =  MutableStateFlow<List<String>>(emptyList())

    init {
        viewModelScope.launch {
            photoRepository.allTags().collect {
                allTags.value = it
            }
        }
    }

    private var pagingSource = PhotosPagingSource(photoRepository)

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    fun setIndex(index: Int) {
        Log.d("Pager", "Pager state is on page $index")
        _currentIndex.value = index
    }

    /**
     * defines how the LazyColumn is displayed
     */
    var scrollPagingData = Pager(PagingConfig(pageSize = 20)) {
        pagingSource
    }.flow.cachedIn(viewModelScope)

    fun submitTag(tag: String) {
        viewModelScope.launch {
            photoRepository.addTag(tag)
        }
    }
}