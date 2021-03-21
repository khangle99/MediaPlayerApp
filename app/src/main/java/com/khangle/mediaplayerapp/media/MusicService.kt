package com.khangle.mediaplayerapp.media

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.repo.MusicServiceRepository
import com.khangle.mediaplayerapp.extension.*
import com.khangle.mediaplayerapp.home.PLAYLISTID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {
    @Inject
    lateinit var musicServiceRepository: MusicServiceRepository

    private var stateBuilder: PlaybackStateCompat.Builder? = null
    private lateinit var notificationManager: MediaNotificationManager
    protected lateinit var mediaSession: MediaSessionCompat
    private lateinit var currentPlayer: Player // su dung de tuong lai them feat cast player
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()
    val deezerBrowserTree: DeezerBrowserTree by lazy {
        DeezerBrowserTree(musicServiceRepository, serviceSope)
    }
    val serverJob = Job()
    val serviceSope = CoroutineScope(serverJob + Dispatchers.Main)

    private val dataSourceFactory: DefaultDataSourceFactory by lazy {
        DefaultDataSourceFactory(
            /* context= */ this,
            Util.getUserAgent(/* context= */ this, UAMP_USER_AGENT), /* listener= */
            null
        )
    }
    private var isForegroundService = false

    private val uAmpAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()
    private val playerListener = PlayerEventListener()

    private val exoPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(uAmpAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    override fun onCreate() {
        super.onCreate()
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }

        mediaSession = MediaSessionCompat(this, "MusicService")
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }

//        stateBuilder = PlaybackStateCompat.Builder()
//                .setActions(
//                        PlaybackStateCompat.ACTION_PLAY
//                                or PlaybackStateCompat.ACTION_PLAY_PAUSE
//                                or PlaybackStateCompat.ACTION_PREPARE_FROM_URI
//                                or PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID
//                                or PlaybackStateCompat.ACTION_PLAY_FROM_URI
//                )
//        mediaSession.setPlaybackState(stateBuilder?.build())
        sessionToken = mediaSession.sessionToken
        notificationManager = MediaNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )
//        mediaSessionConnector = MediaSessionConnector(mediaSession)
//        mediaSessionConnector.setPlaybackPreparer(MusicPlaybackPreparer())

        //  mediaSessionConnector.setQueueNavigator(UampQueueNavigator(mediaSession))

        setupPlayer()
        notificationManager.showNotificationForPlayer(currentPlayer)
        //    mediaSessionConnector.setPlayer(currentPlayer)
        mediaSession.setCallback(callback)
    }

    private fun setupPlayer() { // tuong lai doi thanh switchPlayer
        currentPlayer = exoPlayer
        val playbackState = currentPlayer.playbackState
        // playlist rong thi stop
        if (currentPlaylistItems.isEmpty()) {
            currentPlayer.stop(/* reset= */true)
        } else if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            preparePlaylist(
                metadataList = currentPlaylistItems,
                itemToPlay = currentPlaylistItems[currentPlayer.currentWindowIndex],
                playWhenReady = currentPlayer.playWhenReady,
                playbackStartPositionMs = currentPlayer.currentPosition
            )
        }

    }

    override fun onTaskRemoved(rootIntent: Intent) {
        // saveRecentSongToStorage()
        super.onTaskRemoved(rootIntent)

        /**
         * By stopping playback, the player will transition to [Player.STATE_IDLE] triggering
         * [Player.EventListener.onPlayerStateChanged] to be called. This will cause the
         * notification to be hidden and trigger
         * [PlayerNotificationManager.NotificationListener.onNotificationCancelled] to be called.
         * The service will then remove itself as a foreground service, and will call
         * [stopSelf].
         */
        currentPlayer.stop(/* reset= */true)
    }

    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }
        serverJob.cancel()
        // Free ExoPlayer resources.
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        deezerBrowserTree.getTracks(parentId, result)
        result.detach()

    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot("jj", null)
    }

    override fun onSearch(
        query: String,
        extras: Bundle?,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        super.onSearch(query, extras, result)

    }


    // ham load toan bo list vao vao player
    private fun preparePlaylist(
        metadataList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat,
        playWhenReady: Any,
        playbackStartPositionMs: Any
    ) {

    }


    val callback = object : MediaSessionCompat.Callback() {
        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {

            super.onPlayFromMediaId(mediaId, extras)
            // truy van voi mediaId
            mediaId?.let {
                serviceSope.launch {
                    val playlistId = extras?.getString(PLAYLISTID)
                    if (playlistId != null) {

                        currentPlaylistItems = deezerBrowserTree.getCurrentPlaylist(playlistId)
                        val find = currentPlaylistItems.find { it.id == mediaId }
                        val windowsIndex = currentPlaylistItems.indexOf(find)
                        val mediaSource = currentPlaylistItems.toMediaSource(dataSourceFactory)
                        exoPlayer.setMediaSource(mediaSource)
                        exoPlayer.prepare()
                        exoPlayer.seekTo(windowsIndex, 0) // chay den item thu index vi tri
                        exoPlayer.playWhenReady = true
                        mediaSession.setMetadata(find)
                        updateCurrentPosition(find!!.duration)
                    } else {
                        val track = musicServiceRepository.getTrack(it)
                        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(track.mediaUri))
                        exoPlayer.playWhenReady = true
                        exoPlayer.setMediaSource(mediaSource)
                        exoPlayer.prepare()
                        mediaSession.setMetadata(track)
                        updateCurrentPosition(track.duration)
                    }

                }
                //    registerReceiver(myNoisyAudioStreamReceiver, intentFilter)
            }


        }


        override fun onPlay() {
            super.onPlay()
            exoPlayer.play()
            updateCurrentPosition()
        }

        val handler = Handler(Looper.getMainLooper())
        private fun updateCurrentPosition(duration: Long = 3000) {
            val currentPosition: Long = currentPlayer.getCurrentPosition()
            if (currentPosition >= 30000) { // do chua co ban quyen
                //    mediaSession.setPlaybackState(playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 30000, 0f).build())
                stopPlaybackStateUpdate()
            } else {
                handler.postDelayed({
                    val playbackState = playbackStateBuilder
                        .setState(PlaybackStateCompat.STATE_PLAYING, currentPosition, 0f)
                        .build()
                    mediaSession.setPlaybackState(playbackState)
                    updateCurrentPosition(duration);

                }, 1000)

            }

        }

        private fun stopPlaybackStateUpdate() {
            handler.removeCallbacksAndMessages(null)
        }


        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
            currentPlayer.seekTo(pos)
            stopPlaybackStateUpdate()

            mediaSession.setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PLAYING, pos, 0f)
                    .build()
            )
            updateCurrentPosition()
        }

        override fun onSetShuffleMode(shuffleMode: Int) {
            super.onSetShuffleMode(shuffleMode)
            exoPlayer.shuffleModeEnabled = shuffleMode == Shuffle.ON.value

        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            if (exoPlayer.hasNext()) {
                exoPlayer.next()
                mediaSession.setMetadata(currentPlaylistItems[exoPlayer.currentWindowIndex])

            }

        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            if (exoPlayer.hasPrevious()) {
                exoPlayer.previous()
                mediaSession.setMetadata(currentPlaylistItems[exoPlayer.currentWindowIndex])
            }
        }

        override fun onSetRepeatMode(repeatMode: Int) {
            super.onSetRepeatMode(repeatMode)
            when (repeatMode) {
                RepeatMode.ALL.value -> {
                    exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
                }
                RepeatMode.ONE.value -> {
                    exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                }
                RepeatMode.OFF.value -> {
                    exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_OFF
                }
                else -> {
                }
            }

        }

        val playbackStateBuilder = PlaybackStateCompat.Builder().setActions(
            PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_PREPARE_FROM_URI
                    or PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID
                    or PlaybackStateCompat.ACTION_PLAY_FROM_URI
        )

        override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
            super.onPlayFromUri(uri, extras)
//          val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri!!)
//            exoPlayer.playWhenReady = true
//            exoPlayer.setMediaSource(mediaSource)
//            exoPlayer.prepare()
//            mediaSession.setPlaybackState(playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0,0f).build())
//          val mediaMetadataCompat = extras?.getParcelable<MediaMetadataCompat>("metadata")
//          mediaSession.setMetadata(mediaMetadataCompat)
        }

        override fun onStop() {
            super.onStop()

            //  unregisterReceiver(myNoisyAudioStreamReceiver)
        }


        override fun onPause() {
            super.onPause()
            stopPlaybackStateUpdate()
            exoPlayer.pause()
        }
    }


/*
    private inner class MusicPlaybackPreparer : MediaSessionConnector.PlaybackPreparer {
        override fun onCommand(player: Player, controlDispatcher: ControlDispatcher, command: String, extras: Bundle?, cb: ResultReceiver?): Boolean {
          return false
        }

        override fun getSupportedPrepareActions(): Long {
          return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                  PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                  PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                  PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
        }


        override fun onPrepare(playWhenReady: Boolean) {
            // load nhac recent
        }

        override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
            serviceSope.launch {
                val track = musicServiceRepository.getTrack(mediaId)
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(track.mediaUri))
                exoPlayer.playWhenReady = true
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.prepare()
            }
        }

        override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
            // load nhac tu search
        }

        override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

    } */

    private inner class PlayerEventListener : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY -> {
                    notificationManager.showNotificationForPlayer(currentPlayer)
                    if (playbackState == Player.STATE_READY) {
                        // When playing/paused save the current media item in persistent
                        // storage so that playback can be resumed between device reboots.
                        // Search for "media resumption" for more information.
                        //  saveRecentSongToStorage()
                        if (!playWhenReady) {
                            // If playback is paused we remove the foreground state which allows the
                            // notification to be dismissed. An alternative would be to provide a
                            // "close" button in the notification which stops playback and clears
                            // the notification.
                            stopForeground(false)
                        }
                    }
                }
                else -> {
                    notificationManager.hideNotification()
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            var message = R.string.generic_error;
            when (error.type) {
                // If the data from MediaSource object could not be loaded the Exoplayer raises
                // a type_source error.
                // An error message is printed to UI via Toast message to inform the user.
                ExoPlaybackException.TYPE_SOURCE -> {
                    message = R.string.error_media_not_found;
                    Log.e(TAG, "TYPE_SOURCE: " + error.sourceException.message)
                }
                // If the error occurs in a render component, Exoplayer raises a type_remote error.
                ExoPlaybackException.TYPE_RENDERER -> {
                    Log.e(TAG, "TYPE_RENDERER: " + error.rendererException.message)
                }
                // If occurs an unexpected RuntimeException Exoplayer raises a type_unexpected error.
                ExoPlaybackException.TYPE_UNEXPECTED -> {
                    Log.e(TAG, "TYPE_UNEXPECTED: " + error.unexpectedException.message)
                }
//                // Occurs when there is a OutOfMemory error.
//                ExoPlaybackException.TYPE_OUT_OF_MEMORY -> {
//                    Log.e(TAG, "TYPE_OUT_OF_MEMORY: " + error.outOfMemoryError.message)
//                }
                // If the error occurs in a remote component, Exoplayer raises a type_remote error.
                ExoPlaybackException.TYPE_REMOTE -> {
                    Log.e(TAG, "TYPE_REMOTE: " + error.message)
                }
            }
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@MusicService.javaClass)
                )

                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    private val intentFilter =
        IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY) // intent filter nhan su kien noisy
    private val myNoisyAudioStreamReceiver = BecomingNoisyReceiver()

    private inner class BecomingNoisyReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                Log.i(TAG, "onReceive: nhan dc noisy")
            }
        }
    }

}


private const val UAMP_USER_AGENT = "useragent" // user agent
private const val TAG = "MusicService"

enum class Shuffle(val value: Int) {
    ON(1),
    OFF(0)
}

enum class RepeatMode(val value: Int) {
    ALL(1),
    OFF(0),
    ONE(2)
}
