package com.khangle.mediaplayerapp.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Album(
    val id: Long = -1,
    val title: String = "",
    @SerializedName("cover_small")
    val smalIconUrl: String = "",
    @SerializedName("cover_big")
    val coverUrl: String = "",
    @SerializedName("tracklist")
    val tracklistUrl: String = "",
    val artist: Artist? = null
): Parcelable
data class AlbumListRespone(
    val total: Int,
    val data: List<Album>,
    val next: String = ""
)

val AlbumDiff = object : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.id == newItem.id
    }

}
