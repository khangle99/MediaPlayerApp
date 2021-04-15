package com.khangle.mediaplayerapp.data.network.retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
class MMTrackMessResponse(
    @SerializedName("message") val message: MMTrackResponse,
) : Parcelable
@Parcelize
class MMTrackResponse(
    @SerializedName("body") val body: MMTrackSearchResponseBody,
) : Parcelable
@Parcelize
class MMTrackSearchResponseBody(
    val track_list: List<MMTrack>
) : Parcelable

@Parcelize
class MMTrack(
    @SerializedName("track") val trackDetail: MMTrackDetail,
    @SerializedName("has_lyrics") val hasLyric: Int
) : Parcelable

@Parcelize
class MMTrackDetail(
    @SerializedName("track_id") val id: String,
) : Parcelable




