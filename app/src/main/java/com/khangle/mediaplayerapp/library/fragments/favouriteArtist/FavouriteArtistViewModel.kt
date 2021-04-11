package com.khangle.mediaplayerapp.library.fragments.favouriteArtist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerBaseUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteArtistViewModel @Inject constructor(private val baseUserService: DeezerBaseUserService) :
    ViewModel() {
    private val _artistList = MutableLiveData<Resource<List<Artist>>>()
    val artistList: LiveData<Resource<List<Artist>>> = _artistList
    fun fetchArtist(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _artistList.postValue(Resource.Loading())
                val favouriteArtist = baseUserService.getFavouriteArtist(token)
                _artistList.postValue(Resource.Success(data = favouriteArtist.data))
            } catch (e: Exception) {
                _artistList.postValue(Resource.Error(e.message!!))
            }
        }
    }
}