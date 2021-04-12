package com.khangle.mediaplayerapp.library.fragments.mainLibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.retrofit.BaseWebservice
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerBaseUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainLibraryViewModel @Inject constructor(private val baseUserService: DeezerBaseUserService, private val baseWebservice: BaseWebservice) :
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
                val recommendArtists1 =  async { baseUserService.getRecommendArtists(token) }
                val recommendTracks1 = async { baseUserService.getRecommendTracks(token) }
                // convert recommend track sang  playable track
                val listRecommend: List<Track> = recommendTracks1.await().data // list nay preview rong
                val playableList: List<Track> = listRecommend.map {
                    async { baseWebservice.getTrack(it.id.toString()) }
                }.awaitAll()
                playableList
                _recommendArtists.postValue(recommendArtists1.await().data)
                _recommendTracks.postValue(playableList)
                _status.postValue(Resource.Success(""))
            } catch (e: Exception) {
                _status.postValue(Resource.Error(e.message!!))
            }
        }
    }
}