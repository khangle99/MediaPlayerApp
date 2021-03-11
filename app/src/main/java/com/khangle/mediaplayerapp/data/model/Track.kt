package com.khangle.mediaplayerapp.data.model

import com.google.gson.annotations.SerializedName

data class Track(
    val id: Int,
    val readable: Boolean,
    val title: String,
    @SerializedName("title_short")
    val titleShort: String,
    val duration: Int,
    val preview: String
)