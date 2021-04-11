package com.khangle.mediaplayerapp.library.fragments.mainLibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerBaseUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainLibraryViewModel @Inject constructor(private val baseUserService: DeezerBaseUserService) :
    ViewModel() {
    private val _recommendTracks = MutableLiveData<List<Track>>()
    private val _recommendArtists = MutableLiveData<List<Artist>>()
   val recommendTracks: LiveData<List<Track>> = _recommendTracks
    val recommendArtists: LiveData<List<Artist>> = _recommendArtists
    private val _status = MutableLiveData<Resource<String>>()
    val status: LiveData<Resource<String>> = _status
    fun fetchRecommend(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _status.postValue(Resource.Loading())
                val recommendArtists1 = baseUserService.getRecommendArtists(token)
                val recommendTracks1 = baseUserService.getRecommendTracks(token)
                _recommendArtists.postValue(recommendArtists1.data)
                _recommendTracks.postValue(recommendTracks1.data)
                _status.postValue(Resource.Success(""))
            } catch (e: Exception) {
                _status.postValue(Resource.Error(e.message!!))
            }
        }


    }
}