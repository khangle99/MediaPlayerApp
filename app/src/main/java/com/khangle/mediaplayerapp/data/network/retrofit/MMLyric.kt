package com.khangle.mediaplayerapp.data.network.retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class MMLyricMessResponse(
    @SerializedName("message") val message: MMLyricResponse,
) : Parcelable
@Parcelize
class MMLyricResponse(
    val body: MMLyricResponseBody,
) : Parcelable

@Parcelize
class MMLyricResponseBody(
    val lyrics: MMLyric
) : Parcelable

@Parcelize
class MMLyric(
    @SerializedName("lyrics_body") val lyricBody: String,
) : Parcelable


