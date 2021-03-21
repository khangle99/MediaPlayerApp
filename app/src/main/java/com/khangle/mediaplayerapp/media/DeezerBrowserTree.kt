package com.khangle.mediaplayerapp.media

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.media.MediaBrowserServiceCompat
import com.khangle.mediaplayerapp.data.repo.MusicServiceRepository
import com.khangle.mediaplayerapp.extension.*
import com.khangle.mediaplayerapp.home.PlaylistId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Load va Luu toan bo cac list da duoc load tu backend
 *
 */
class DeezerBrowserTree constructor(val musicServiceRepository: MusicServiceRepository, val scope: CoroutineScope) {
    // list cac playlist dc load, se dc su dung load vao(currentPlaylist) khi user click
    // de biet gui playlist nao thi se su dung bien flag enum PLAYLISTID
   lateinit var chartTracks : List<MediaMetadataCompat>
    fun getTracks(parentId: String,result: MediaBrowserServiceCompat.Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        if(parentId == "editorialTrack") {
            scope.launch {
                chartTracks = musicServiceRepository.getChartsTracks()

                result.sendResult(chartTracks.map { item ->
                    val des = MediaDescriptionCompat.Builder()
                        .setTitle(item.title)
                        .setSubtitle(item.displaySubtitle)
                        .setDescription(item.description.description)
                        .setMediaId(item.id)
                        .setIconUri(item.displayIconUri)
                        .setExtras(item.bundle)
                        .setMediaUri(item.albumArtUri) // dung icon art thay cho media, vi k can media uri
                        .build()
                    MediaBrowserCompat.MediaItem(des, item.flag)
                }.toMutableList())
            }

        }
    }

    fun getCurrentPlaylist(playlistId: String): List<MediaMetadataCompat> {
        when (playlistId) {
            PlaylistId.CHART.value -> return chartTracks
            else -> return emptyList()
        }
    }
}