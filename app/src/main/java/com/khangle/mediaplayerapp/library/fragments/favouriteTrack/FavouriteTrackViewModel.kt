package com.khangle.mediaplayerapp.library.fragments.favouriteTrack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerBaseUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteTrackViewModel @Inject constructor(private val baseUserService: DeezerBaseUserService): ViewModel() {
    private val _trackList = MutableLiveData<Resource<List<Track>>>()
    val trackList: LiveData<Resource<List<Track>>> = _trackList
    fun fetchFavouriteTracks(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _trackList.postValue(Resource.Loading())
                val favouriteTracks = baseUserService.getFavouriteTracks(token)
                _trackList.postValue(Resource.Success(data = favouriteTracks.data))
            } catch (e: Exception) {
                _trackList.postValue(Resource.Error(e.message!!))
            }

        }

    }
}