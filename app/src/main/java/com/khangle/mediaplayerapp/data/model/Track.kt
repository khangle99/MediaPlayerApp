package com.khangle.mediaplayerapp.data.model

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.google.gson.annotations.SerializedName
import com.khangle.mediaplayerapp.extension.*
import kotlinx.parcelize.Parcelize
import java.util.concurrent.TimeUnit

@Parcelize
 class Track(
   val id: Long = -1,
   val readable: Boolean = false,
   val title: String = "",
   @SerializedName("title_short")
    val titleShort: String = "",
   val duration: Long = -1,
   val preview: String = "",
   var album: Album? = null,
   val artist: Artist? = null
): Parcelable
data class TrackListRespone(
   val total: Int,
   val data: List<Track>,
   val next: String = ""
)
/*
* {
        "id": 1140154392,
        "title": "Alone",
        "title_short": "Alone",
        "title_version": "",
        "link": "https://www.deezer.com/track/1140154392",
        "duration": 172,
        "rank": 431522,
        "explicit_lyrics": false,
        "explicit_content_lyrics": 0,
        "explicit_content_cover": 0,
        "preview": "https://cdns-preview-4.dzcdn.net/stream/c-4a337f3e360ca5be691f9ed7d4ffb69a-3.mp3",
        "md5_image": "5e161cdfae36fc472fedc395a6ac4ee9",
        "position": 1,
        "artist": {
          "id": 14752749,
          "name": "Garrin Mater",
          "link": "https://www.deezer.com/artist/14752749",
          "picture": "https://api.deezer.com/artist/14752749/image",
          "picture_small": "https://cdns-images.dzcdn.net/images/artist/41eb2c6631485fa08f312bd090e6858b/56x56-000000-80-0-0.jpg",
          "picture_medium": "https://cdns-images.dzcdn.net/images/artist/41eb2c6631485fa08f312bd090e6858b/250x250-000000-80-0-0.jpg",
          "picture_big": "https://cdns-images.dzcdn.net/images/artist/41eb2c6631485fa08f312bd090e6858b/500x500-000000-80-0-0.jpg",
          "picture_xl": "https://cdns-images.dzcdn.net/images/artist/41eb2c6631485fa08f312bd090e6858b/1000x1000-000000-80-0-0.jpg",
          "radio": false,
          "tracklist": "https://api.deezer.com/artist/14752749/top?limit=50",
          "type": "artist"
        }
* */


fun MediaMetadataCompat.Builder.from(track: Track, audioSessionId1: Int): MediaMetadataCompat.Builder {
   // The duration from the JSON is given in seconds, but the rest of the code works in
   // milliseconds. Here's where we convert to the proper units.
   val durationMs = TimeUnit.SECONDS.toMillis(track.duration)

   id = track.id.toString()
   title = track.title
   artist = track.artist?.name
   album = track.album?.title
   duration = durationMs
   //  genre = track.genre
   mediaUri = track.preview
   albumArtUri = track.album?.coverUrl
   // trackNumber = track.album. trong chi tiet album moi co thuoc tinh
   //  trackCount = track.totalTrackCount trong chi tiet album moi co thuoc tinh
   flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

   // To make things easier for *displaying* these, set the display properties as well.
   displayTitle = track.title
   displaySubtitle = track.artist?.name
   //  displayDescription = track.album
   displayIconUri = track.album?.smalIconUrl
    audioSessionId = audioSessionId1
   // Add downloadStatus to force the creation of an "extras" bundle in the resulting
   // MediaMetadataCompat object. This is needed to send accurate metadata to the
   // media session during updates.
   downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED

   // Allow it to be used in the typical builder style.
   return this
}
fun MediaMetadataCompat.toTrack(): Track {
   // The duration from the JSON is given in seconds, but the rest of the code works in
   // milliseconds. Here's where we convert to the proper units.
//    val durationMs = TimeUnit.SECONDS.toMillis(track.duration)
//
//    id = track.id.toString()
//    title = track.title
//    artist = track.artist?.name
//    album = track.album?.title
//    duration = durationMs
//    //  genre = track.genre
//    mediaUri = track.preview
//    albumArtUri = track.album?.coverUrl
//    // trackNumber = track.album. trong chi tiet album moi co thuoc tinh
//    //  trackCount = track.totalTrackCount trong chi tiet album moi co thuoc tinh
//    flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
//
//    // To make things easier for *displaying* these, set the display properties as well.
//    displayTitle = track.title
//    displaySubtitle = track.artist?.name
//    //  displayDescription = track.album
//    displayIconUri = track.album?.smalIconUrl
//
//    // Add downloadStatus to force the creation of an "extras" bundle in the resulting
//    // MediaMetadataCompat object. This is needed to send accurate metadata to the
//    // media session during updates.
//    downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED

   // Allow it to be used in the typical builder style.
   val artist = Artist(name = this.displaySubtitle!!)
   val album = Album(title = album!!, coverUrl = albumArtUri!!.toString(), smalIconUrl = displayIconUri.toString())
   return Track(id!!.toLong(), true, displayTitle!!, title!!, duration, mediaUri.toString(), album, artist) // trach chi co tac dung show UI nen k can info nhieu
}