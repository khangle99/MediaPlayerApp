package com.khangle.mediaplayerapp.newReleaseDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Album
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(val repository: DeezerRepository): ViewModel() {
    private val _albumTracks = MutableLiveData<List<Track>>()
    val albumTracks: LiveData<List<Track>> = _albumTracks
    fun loadAlbumTrack(album: Album) {
        viewModelScope.launch(Dispatchers.IO) {
            val chartTrack = repository.getAlbumTracks(album.id.toInt())
            chartTrack.forEach {
                it.album = album
            }
            _albumTracks.postValue(chartTrack)
        }

    }

}