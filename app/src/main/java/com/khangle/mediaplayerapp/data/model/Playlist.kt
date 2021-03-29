package com.khangle.mediaplayerapp.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
 class Playlist(
        val id: Long = -1,
        val title: String = "",
        @SerializedName("picture_small")
        val smalIconUrl: String = "",
        @SerializedName("picture_big")
        val coverUrl: String = "",
        @SerializedName("tracklist")
        val tracklistUrl: String = "",
        @SerializedName("nb_tracks")
        val numberOfTrack: String = "0"
): Parcelable
data class PlaylistListRespone(
       val total: Int,
       val data: List<Playlist>,
       val next: String = ""
)

val PlaylistDiff = object : DiffUtil.ItemCallback<Playlist>() {
       override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
              return oldItem.id == newItem.id
       }

       override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
              return oldItem.id == newItem.id
       }

}

/*
* "id": 185904782,
          "title": "Alone",
          "cover": "https://api.deezer.com/album/185904782/image",
          "cover_small": "https://cdns-images.dzcdn.net/images/cover/5e161cdfae36fc472fedc395a6ac4ee9/56x56-000000-80-0-0.jpg",
          "cover_medium": "https://cdns-images.dzcdn.net/images/cover/5e161cdfae36fc472fedc395a6ac4ee9/250x250-000000-80-0-0.jpg",
          "cover_big": "https://cdns-images.dzcdn.net/images/cover/5e161cdfae36fc472fedc395a6ac4ee9/500x500-000000-80-0-0.jpg",
          "cover_xl": "https://cdns-images.dzcdn.net/images/cover/5e161cdfae36fc472fedc395a6ac4ee9/1000x1000-000000-80-0-0.jpg",
          "md5_image": "5e161cdfae36fc472fedc395a6ac4ee9",
          "tracklist": "https://api.deezer.com/album/185904782/tracks",
* */