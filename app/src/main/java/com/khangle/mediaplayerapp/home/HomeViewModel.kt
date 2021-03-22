package com.khangle.mediaplayerapp.home

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khangle.mediaplayerapp.media.ERROR_CODE
import com.khangle.mediaplayerapp.media.ERROR_CODE_KEY
import com.khangle.mediaplayerapp.media.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val musicServiceConnection: MusicServiceConnection) : ViewModel() {
    private val _chartTracks = MutableLiveData<List<MediaBrowserCompat.MediaItem>>();
    val chartTracks: LiveData<List<MediaBrowserCompat.MediaItem>> = _chartTracks
    init {
        init()
    }

    private val TAG = "HomeViewModel"
    lateinit var  subscriptionCallback : MediaBrowserCompat.SubscriptionCallback
    fun init() {
        subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                _chartTracks.postValue(children)
            }


            override fun onError(parentId: String, options: Bundle) {
                super.onError(parentId, options)
                // kiem tra loi
                val code = options.getInt(ERROR_CODE_KEY)
                when (code) {
                    ERROR_CODE.NO_CONNECTIVITY.value -> {
                        Log.e(TAG, "onError: loi khong co connectivity")
                    }
                    else -> {}
                }
            }

        }
        // lang nghe intenet
        musicServiceConnection.subscribe("editorialTrack", subscriptionCallback)
    }
    fun refresh() {
        musicServiceConnection.unsubcribe("editorialTrack", subscriptionCallback) // k huy thi k chay ??
        musicServiceConnection.subscribe("editorialTrack", subscriptionCallback)
    }


    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubcribe("editorialTrack", subscriptionCallback)
    }



}