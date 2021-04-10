package com.khangle.mediaplayerapp.data.network.retrofit

import com.khangle.mediaplayerapp.data.model.ArtistListRespone
import com.khangle.mediaplayerapp.data.model.TrackListRespone
import com.khangle.mediaplayerapp.data.model.UserInfo
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DeezerBaseUserService {
    // cac ham user
    suspend fun getUserInfo(token: String): UserInfo

    // them xem xoa artist
    suspend fun getFavouriteArtist(token: String): ArtistListRespone
    suspend fun addFavouriteArtist(token: String, artistId: Long)
    suspend fun removeFavouriteArtist(token: String, artistId: Long)

    // them xem xoa favourite track
    suspend fun getFavouriteTracks(token: String): TrackListRespone
    suspend fun addFavouriteTracks(token: String, trackId: Long)
    suspend fun removeFavouriteTracks(token: String, trackId: Long)

    // get recommand artist
    suspend fun getRecommendArtists(token: String): ArtistListRespone

    //get recommend track
    suspend fun getReommendTracks(token: String): TrackListRespone

}

interface DeezerUserService : DeezerBaseUserService {
    @GET("me/")
    override suspend fun getUserInfo(@Query("access_token") token: String): UserInfo

    @GET("me/artists")
    override suspend fun getFavouriteArtist(@Query("access_token") token: String): ArtistListRespone
    @POST("me/artists")
    override suspend fun addFavouriteArtist(@Query("access_token") token: String, @Query("artist_id") artistId: Long)
    @DELETE("me/artists")
    override suspend fun removeFavouriteArtist(@Query("access_token") token: String,@Query("artist_id") artistId: Long)

    @GET("me/tracks")
    override suspend fun getFavouriteTracks(@Query("access_token") token: String): TrackListRespone
    @POST("me/tracks")
    override suspend fun addFavouriteTracks(@Query("access_token") token: String, @Query("track_id")  trackId: Long)
    @DELETE("me/tracks")
    override suspend fun removeFavouriteTracks(@Query("access_token") token: String, @Query("track_id") trackId: Long)
    @GET("me/recommendations/tracks")
    override suspend fun getRecommendArtists(@Query("access_token") token: String): ArtistListRespone
    @GET("me/recommendations/artists")
    override suspend fun getReommendTracks(@Query("access_token") token: String): TrackListRespone
}