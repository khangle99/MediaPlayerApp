package com.khangle.mediaplayerapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Album
import com.khangle.mediaplayerapp.data.model.Genre
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.okhttp.NoConnectivityException
import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: DeezerRepository) : ViewModel() {
    private val _suggestionTracks = MutableLiveData<List<Track>>();
    val suggestionTracks: LiveData<List<Track>> = _suggestionTracks
    private val _genreList = MutableLiveData<List<Genre>>();
    val genreList: LiveData<List<Genre>> = _genreList
    private val _newReleaseAlbums = MutableLiveData<List<Album>>();
    val newReleaseAlbums: LiveData<List<Album>> = _newReleaseAlbums

    private val _error = MutableLiveData<String>();
    val error: LiveData<String> = _error
    private val _isLoading = MutableLiveData<Boolean>();
    val isLoading: LiveData<Boolean> = _isLoading


    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_newReleaseAlbums.value == null) {  _isLoading.postValue(true) }
                val newReleaseAlbum = async { repository.getNewReleaseAlbum() }
                val genres = async { repository.getGenres() }
                val chartsTracks = async { repository.getSuggestionTracks() }

                _newReleaseAlbums.postValue(newReleaseAlbum.await())
                _genreList.postValue(genres.await())
                _suggestionTracks.postValue(chartsTracks.await())
                _isLoading.postValue(false)
            } catch (e: NoConnectivityException) {
                _error.postValue(e.message)
                _isLoading.postValue(false)
            }

        }
    }





}