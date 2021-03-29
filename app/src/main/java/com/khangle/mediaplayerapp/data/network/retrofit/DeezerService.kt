package com.khangle.mediaplayerapp.data.network.retrofit

import com.khangle.mediaplayerapp.data.model.ArtistListRespone
import com.khangle.mediaplayerapp.data.model.PlaylistListRespone
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.model.TrackListRespone
import com.khangle.mediaplayerapp.data.network.ChartSelectionReponse
import com.khangle.mediaplayerapp.data.network.GenreResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BaseWebservice {
    suspend fun getCharts(): ChartSelectionReponse
    suspend fun getTrack(trackId: String) : Track
    suspend fun getGenres(): GenreResponse
    suspend fun searchTrack(querry: String, index: Int): TrackListRespone
    suspend fun searchPlaylist(querry: String, index: Int): PlaylistListRespone
    suspend fun searchArtist(querry: String, index: Int): ArtistListRespone
    suspend fun relateArtist(artistId: String): ArtistListRespone
    suspend fun topTrackOfArtist(artistId: String, index: Int, limit: Int= 15): TrackListRespone
    suspend fun playlistTrack(playlistId: String, index: Int, limit: Int= 15): TrackListRespone

}

interface DeezerService: BaseWebservice {
    @GET("/search/playlist")
    override suspend fun searchPlaylist(@Query("q") querry: String, @Query("index")index: Int): PlaylistListRespone
    @GET("/search/track")
    override suspend fun searchTrack(@Query("q") querry: String, @Query("index")index: Int): TrackListRespone
    @GET("/search/artist")
    override suspend fun searchArtist(@Query("q") querry: String, @Query("index")index: Int): ArtistListRespone
    @GET("/artist/{id}/top")
    override suspend fun topTrackOfArtist(@Path("id") artistId: String, @Query("index")index: Int, @Query("limit") limit: Int): TrackListRespone
    @GET("/artist/{id}/related")
    override suspend fun relateArtist(@Path("id")  artistId: String): ArtistListRespone
    @GET("/genre/")
    override suspend fun getGenres(): GenreResponse
    @GET("/editorial/0/charts")
    override suspend fun getCharts(): ChartSelectionReponse // khong co tham so phan trang
    @GET("/track/{id}")
    override suspend fun getTrack(@Path("id")  trackId: String): Track
    @GET("/playlist/{id}}/tracks")
    override suspend fun playlistTrack(@Path("id") playlistId: String, @Query("index")index: Int, @Query("limit") limit: Int): TrackListRespone



}