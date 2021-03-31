package com.khangle.mediaplayerapp.discovery.fragments.PlaylistDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(val deezerRepository: DeezerRepository): ViewModel() {
    private val _artistTrack = MutableLiveData<List<Track>>()
    val artistTrack: LiveData<List<Track>> = _artistTrack

    fun loadTrack(playlistId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val compatArtistTrackList = deezerRepository.getCompatPlaylistTracks(playlistId)
            _artistTrack.postValue(compatArtistTrackList)
        }
    }
}