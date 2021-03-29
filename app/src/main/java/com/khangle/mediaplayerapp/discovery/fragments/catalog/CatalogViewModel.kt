package com.khangle.mediaplayerapp.discovery.fragments.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Genre
import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(private val repository: DeezerRepository): ViewModel() {
    private val _genreList = MutableLiveData<List<Genre>>()
    val genreList: LiveData<List<Genre>> = _genreList
    init {
        refresh()

    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val genres = repository.getGenres()
            _genreList.postValue(genres)
        }
    }
}