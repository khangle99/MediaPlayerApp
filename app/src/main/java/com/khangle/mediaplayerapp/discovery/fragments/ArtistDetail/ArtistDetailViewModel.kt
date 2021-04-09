package com.khangle.mediaplayerapp.discovery.fragments.ArtistDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.okhttp.NoConnectivityException
import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(val deezerRepository: DeezerRepository): ViewModel() {
    private val _relateArtist = MutableLiveData<List<Artist>>()
    val relateArtist: LiveData<List<Artist>> = _relateArtist
    private val _artistTrack = MutableLiveData<List<Track>>()
    val artistTrack: LiveData<List<Track>> = _artistTrack
    private val _error = MutableLiveData<String>().apply { value = ""  }
    val error: LiveData<String> = _error
    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading : LiveData<Boolean> = _isLoading
    fun loadTrackAndRelateArtist(artistId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_artistTrack.value == null) {_isLoading.postValue(true)}
                val compatArtistTrackList = deezerRepository.getCompatArtistTrackList(artistId)
                _artistTrack.postValue(compatArtistTrackList)
                val relateArtist = deezerRepository.getRelateArtist(artistId)
                _relateArtist.postValue(relateArtist)
                _isLoading.postValue(false)
            } catch (e: NoConnectivityException) {
                _error.postValue(e.message)
                _isLoading.postValue(false)
            }

        }
    }

}