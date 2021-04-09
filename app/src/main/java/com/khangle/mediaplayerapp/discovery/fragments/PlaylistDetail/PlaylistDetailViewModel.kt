package com.khangle.mediaplayerapp.discovery.fragments.PlaylistDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.okhttp.NoConnectivityException
import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(val deezerRepository: DeezerRepository): ViewModel() {
    private val _artistTrack = MutableLiveData<Resource<List<Track>>>()
    val artistTrack: LiveData<Resource<List<Track>>> = _artistTrack

    fun loadTrack(playlistId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
               if (_artistTrack.value == null) { _artistTrack.postValue(Resource.Loading())}
                val compatArtistTrackList = deezerRepository.getCompatPlaylistTracks(playlistId)
                _artistTrack.postValue(Resource.Success(data = compatArtistTrackList))
            } catch (e: NoConnectivityException) {
                _artistTrack.postValue(Resource.Error(e.message))
            }
        }
    }
}