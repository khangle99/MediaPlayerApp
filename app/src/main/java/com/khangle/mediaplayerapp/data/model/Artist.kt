package com.khangle.mediaplayerapp.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
 class Artist(
        val id: Long = -1,
        val name: String = "",
        @SerializedName("picture_medium")
        val pictureUrl: String = "",
        @SerializedName("tracklist")
        val trackListUrl: String = "",
        @SerializedName("nb_album")
        val albumCount: String = ""
): Parcelable
 val ArtistDiff = object : DiffUtil.ItemCallback<Artist>() {
       override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
             return oldItem.id == newItem.id
       }

       override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
              return oldItem.id == newItem.id
       }

}

data class ArtistListRespone(
    val total: Int,
    val data: List<Artist>,
    val next: String = ""
)
/*
* "artist": {
          "id": 5297745,
          "name": "Ngoc Lan",
          "link": "https://www.deezer.com/artist/5297745",
          "picture": "https://api.deezer.com/artist/5297745/image",
          "picture_small": "https://cdns-images.dzcdn.net/images/artist/a50fa7a6e9d77a54a33168011b4af00e/56x56-000000-80-0-0.jpg",
          "picture_medium": "https://cdns-images.dzcdn.net/images/artist/a50fa7a6e9d77a54a33168011b4af00e/250x250-000000-80-0-0.jpg",
          "picture_big": "https://cdns-images.dzcdn.net/images/artist/a50fa7a6e9d77a54a33168011b4af00e/500x500-000000-80-0-0.jpg",
          "picture_xl": "https://cdns-images.dzcdn.net/images/artist/a50fa7a6e9d77a54a33168011b4af00e/1000x1000-000000-80-0-0.jpg",
          "radio": false,
          "tracklist": "https://api.deezer.com/artist/5297745/top?limit=50",
          "type": "artist"
        }
* */