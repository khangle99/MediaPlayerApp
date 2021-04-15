package com.khangle.mediaplayerapp.data.network.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface MMBaseWebService {
    suspend fun getTrackByTitleAndArtistName(title: String, artistName: String): MMTrackMessResponse
    suspend fun getLyricByTrackId(id: String): MMLyricMessResponse
}

interface MMWebservice: MMBaseWebService {
    @GET("track.search?apikey=d0fddfba750f208185ca77e14ca4436e&page=0&page_size=1&f_has_lyrics=1")
    override suspend fun getTrackByTitleAndArtistName(@Query("q_track") title: String,@Query("q_artist") artistName: String): MMTrackMessResponse
    @GET("track.lyrics.get?apikey=d0fddfba750f208185ca77e14ca4436e")
    override suspend fun getLyricByTrackId(@Query("track_id") id: String): MMLyricMessResponse
}