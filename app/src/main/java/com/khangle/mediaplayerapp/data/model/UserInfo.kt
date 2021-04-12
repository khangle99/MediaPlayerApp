package com.khangle.mediaplayerapp.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
class UserInfo(val id: Long = -1, val name: String = "", val email: String = "", @SerializedName("picture_medium") val  avartar: String = ""): Parcelable