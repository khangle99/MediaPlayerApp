package com.khangle.mediaplayerapp.discovery.fragments.ArtistDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.data.model.Track
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

    fun loadTrack(artistId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val compatArtistTrackList = deezerRepository.getCompatArtistTrackList(artistId)
            _artistTrack.postValue(compatArtistTrackList)
        }
    }
    fun loadRelateArtist(artistId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val relateArtist = deezerRepository.getRelateArtist(artistId)
            _relateArtist.postValue(relateArtist)
        }
    }
}