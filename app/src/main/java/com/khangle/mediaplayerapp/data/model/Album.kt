package com.khangle.mediaplayerapp.data.model

import com.google.gson.annotations.SerializedName

 class Album(
        val id: Long = -1,
        val title: String = "",
        @SerializedName("cover_small")
        val smalIconUrl: String = "",
        @SerializedName("cover_big")
        val coverUrl: String = "",
        @SerializedName("tracklist")
        val tracklistUrl: String = ""
)


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