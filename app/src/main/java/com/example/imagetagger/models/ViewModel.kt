package com.example.imagetagger.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imagetagger.repositories.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val data = MutableLiveData<String>()
    val observableData: LiveData<String> = data

    fun fetchData() {
        data.value = photoRepository.getData()
    }

    fun getImages() {

    }

}