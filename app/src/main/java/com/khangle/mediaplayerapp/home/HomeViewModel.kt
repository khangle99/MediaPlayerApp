package com.khangle.mediaplayerapp.home

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
class HomeViewModel @Inject constructor(val repository: DeezerRepository) : ViewModel() {
    private val _chartTracks = MutableLiveData<List<Track>>();
    val chartTracks: LiveData<List<Track>> = _chartTracks
    init {
        refresh()
    }

    private val TAG = "HomeViewModel"


    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val chartsTracks = repository.getChartsTracks()
            _chartTracks.postValue(chartsTracks)
        }
    }



}