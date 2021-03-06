package com.khangle.mediaplayerapp.home.fragments.chartDetail

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
class ChartDetailDialogViewModel @Inject constructor(val repository: DeezerRepository): ViewModel() {
    private val _chartTracks = MutableLiveData<Resource<List<Track>>>()
    val chartTracks: LiveData<Resource<List<Track>>> = _chartTracks
    fun loadChartTracks(genreId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _chartTracks.postValue(Resource.Loading())
                val chartTrack = repository.getChartTrack(genreId)
                _chartTracks.postValue(Resource.Success(data = chartTrack))
            } catch (e: NoConnectivityException) {
                _chartTracks.postValue(Resource.Error(e.message))
            }
        }

    }
}