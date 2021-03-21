package com.khangle.mediaplayerapp.home

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khangle.mediaplayerapp.media.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val musicServiceConnection: MusicServiceConnection) : ViewModel() {
    private val _chartTracks = MutableLiveData<List<MediaBrowserCompat.MediaItem>>();
    val chartTracks: LiveData<List<MediaBrowserCompat.MediaItem>> = _chartTracks
    init {
        load()
    }

    lateinit var  subscriptionCallback : MediaBrowserCompat.SubscriptionCallback
    fun load() {
        subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                _chartTracks.postValue(children)
            }
        }
        musicServiceConnection.subscribe("editorialTrack", subscriptionCallback)
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubcribe("editorialTrack", subscriptionCallback)
    }



}