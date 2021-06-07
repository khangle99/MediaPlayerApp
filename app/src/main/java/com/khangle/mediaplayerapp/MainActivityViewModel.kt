package com.khangle.mediaplayerapp

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.*
import com.khangle.mediaplayerapp.data.model.Genre
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerBaseUserService
import com.khangle.mediaplayerapp.data.network.retrofit.MMBaseWebService
import com.khangle.mediaplayerapp.extension.artist
import com.khangle.mediaplayerapp.extension.title
import com.khangle.mediaplayerapp.media.IS_CHANGE_PLAYLIST
import com.khangle.mediaplayerapp.media.MusicServiceConnection
import com.khangle.mediaplayerapp.media.NOTHING_PLAYING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val musicServiceConnection: MusicServiceConnection, val baseUserService: DeezerBaseUserService,
    val mmBaseWebService: MMBaseWebService
) :
    ViewModel() {
    val genreList = mutableListOf<Genre>()
    val metadataBuilder = MediaMetadataCompat.Builder()

    // livedata cho UI
    private var _metadata = MutableLiveData<MediaMetadataCompat>()
    var metadata = _metadata

    private var _state = MutableLiveData<PlaybackStateCompat>()
    var state = _state

    private var currentPlaylist: List<Track>? = null // hien thi tren UI

    private val _lyricCurrentTrackLyric = MutableLiveData<String>()
    val lyricCurrentTrackLyric: LiveData<String> = _lyricCurrentTrackLyric
    init {
        _metadata.value = NOTHING_PLAYING
        musicServiceConnection.also {
            playbackStateObserver = Observer<PlaybackStateCompat> {
                _state.value = it
                Log.i("bufferpo", ": ${it.bufferedPosition}")
            }
            metadataObserver = Observer<MediaMetadataCompat> {
                // load lyric
                if (it.title != null && it.artist != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val res =
                            mmBaseWebService.getTrackByTitleAndArtistName(it.title!!, it.artist!!)
                        if ( res.message.body.track_list.size > 0) {
                            val lyric =
                                mmBaseWebService.getLyricByTrackId(res.message.body.track_list[0].trackDetail.id)
                           _lyricCurrentTrackLyric.postValue(lyric.message.body.lyrics.lyricBody)
                        } else {
                            _lyricCurrentTrackLyric.postValue("")
                        }
                    }
                    _metadata.value = it
                }

            }
            it.nowPlaying.observeForever(metadataObserver)
            it.playbackState.observeForever(playbackStateObserver)

        }
    }

    var playbackStateObserver: Observer<PlaybackStateCompat>
    var metadataObserver: Observer<MediaMetadataCompat>

    /**
     * playlist de hien cho UI, bundle de soan data cho backend
     */
    fun play(track: Track, playlist: List<Track>) {
        // chua co logic item nao dang click thi k lam gi, chi chay khi item moi dc click
        val bundle = Bundle()
        if (musicServiceConnection.isConnected.value == true) {
            // logic kiem tra list co doi thi them flag khong thi thoi
            if (playlist.equals(currentPlaylist)) {
                bundle.putBoolean(IS_CHANGE_PLAYLIST, false)
            } else {
                currentPlaylist = playlist
                bundle.putBoolean(IS_CHANGE_PLAYLIST, true)
                bundle.putParcelableArrayList("myKey", ArrayList(playlist)) // gui list len service
            }

            musicServiceConnection.transportControls.playFromMediaId(
                track.id.toString(),
                bundle
            ) // gui xuong luon ca playlist de k can request lai

        }
    }

    fun setShuffle(mode: Int) {
        if (musicServiceConnection.isConnected.value == true) {
            musicServiceConnection.setShuffle(mode)
        }
    }

    fun setRepeat(mode: Int) {
        if (musicServiceConnection.isConnected.value == true) {
            musicServiceConnection.setRepeat(mode)
        }
    }

    fun play() {
        if (musicServiceConnection.isConnected.value == true) {
            musicServiceConnection.play()
        }
    }

    fun pause() {
        if (musicServiceConnection.isConnected.value == true) {
            musicServiceConnection.pause()
        }
    }

    fun seekTo(pos: Long) {
        if (musicServiceConnection.isConnected.value == true) {
            musicServiceConnection.seekTo(pos)
        }
    }


    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.nowPlaying.removeObserver(metadataObserver)
        musicServiceConnection.playbackState.removeObserver(playbackStateObserver)
    }

    fun next() {
        musicServiceConnection.next()
    }

    fun previous() {
        musicServiceConnection.previous()
    }

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken
    fun setToken(token: String) {
        _accessToken.value = token
    }

}