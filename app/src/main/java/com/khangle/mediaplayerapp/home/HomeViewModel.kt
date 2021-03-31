package com.khangle.mediaplayerapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Album
import com.khangle.mediaplayerapp.data.model.Genre
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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


    private val TAG = "HomeViewModel"


    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val newReleaseAlbum = repository.getNewReleaseAlbum()
            _newReleaseAlbums.postValue(newReleaseAlbum)
            val genres = repository.getGenres()
            _genreList.postValue(genres)

            val chartsTracks = repository.getSuggestionTracks()
            _suggestionTracks.postValue(chartsTracks)
        }
    }



}