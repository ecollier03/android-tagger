package com.example.imagetagger.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.imagetagger.repositories.PhotoRepository
import com.example.imagetagger.repositories.PhotosPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    var pagingData = Pager(PagingConfig(pageSize = 20)) {
        pagingSource
    }.flow.cachedIn(viewModelScope)


    fun fetchAllPhotos() {
        viewModelScope.launch {
            val files = photoRepository.getImages(1, 2)
            _files.value = files
            pagingSource.invalidate()
            Log.i("VM", "Got photos and invalidated pager")
        }
    }

    fun submitTag(tag: String) {
        viewModelScope.launch {
            photoRepository.addTag(tag)
        }
    }
}