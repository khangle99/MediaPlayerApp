package com.khangle.mediaplayerapp.data.repo

import android.support.v4.media.MediaMetadataCompat
import com.khangle.mediaplayerapp.data.model.from
import com.khangle.mediaplayerapp.data.network.retrofit.BaseWebservice
import javax.inject.Inject

/**
 * repo danh cho service truy van, thao tac du lieu voi metadata
 */
interface MusicServiceRepository {
    suspend fun getChartsTracks(): List<MediaMetadataCompat>
    suspend fun getTrack(trackId: String): MediaMetadataCompat
}

class MusicServiceRepositoryImp @Inject constructor(private val baseWebservice: BaseWebservice) : MusicServiceRepository {
    val metadataBuilder = MediaMetadataCompat.Builder()
    override suspend fun getChartsTracks(): List<MediaMetadataCompat> {
        return baseWebservice.getCharts().tracks.data.map {
            metadataBuilder.from(it).build()
        }
    }

    override suspend fun getTrack(trackId: String): MediaMetadataCompat {
        return metadataBuilder.from(baseWebservice.getTrack(trackId)).build()
    }
}