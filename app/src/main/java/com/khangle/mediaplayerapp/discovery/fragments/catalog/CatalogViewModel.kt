package com.khangle.mediaplayerapp.discovery.fragments.catalog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Genre
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.data.network.okhttp.NoConnectivityException
import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(private val repository: DeezerRepository): ViewModel() {
    private val _genreList = MutableLiveData<Resource<List<Genre>>>()
    val genreList: LiveData<Resource<List<Genre>>> = _genreList

     fun refresh() {
         Log.i("d", "refresh: lai data")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_genreList.value == null) {_genreList.postValue(Resource.Loading())}
                val genres = repository.getGenres()
                _genreList.postValue(Resource.Success(data = genres))
            } catch (e: NoConnectivityException) {
                _genreList.postValue(Resource.Error(e.message))
            }

        }
    }
}