package com.khangle.mediaplayerapp

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.khangle.mediaplayerapp.customview.PlayButton
import com.khangle.mediaplayerapp.customview.RepeatButton
import com.khangle.mediaplayerapp.data.model.toTrack

import com.khangle.mediaplayerapp.databinding.ActivityMainBinding
import com.khangle.mediaplayerapp.media.RepeatMode
import com.khangle.mediaplayerapp.media.Shuffle
import com.khangle.mediaplayerapp.navigation.KeepStateNavigator
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    val mainActivityViewModel: MainActivityViewModel by viewModels()
    lateinit var motionLayout: MotionLayout
    lateinit var bottomMotionLayout: MotionLayout

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setLifecycleOwner(this)
        val navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        // setup custom navigator
        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider += navigator
        navController.setGraph(R.navigation.mobile_navigation)
        binding.navView.setupWithNavController(navController)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        //  navView.setupWithNavController(navController)
        // kiem tra va doi lai state cho dung: chua lam
        motionLayout = binding.motionLay
        bottomMotionLayout = binding.container
        setListener(BottomSheetBehavior.from(binding.motionLay))
        binding.target = target
        binding.navView.setBackgroundColor(Color.BLACK)

        // bottomState.state = BottomSheetBehavior.STATE_COLLAPSED
        mainActivityViewModel.metadata.observe(this, {
            // convert sang track de UI de su dung
            if (!it.description.mediaId.equals("")) {
                motionLayout.visibility = View.VISIBLE
                binding.track = it.toTrack()
                binding.progressBar.progress = 0
                binding.trackSeek.progress = 0
            }
        })
        mainActivityViewModel.state.observe(this, observer)
    }

    val observer = Observer<PlaybackStateCompat> {
        if (it.state == PlaybackStateCompat.STATE_PLAYING){
            binding.trackSeek.progress = it.position.toInt()
            binding.progressBar.progress = it.position.toInt()
            val progress = (it.position /1000 ).toInt()
            val time = timeToString(progress)
            binding.current.text = time
            binding.button.isPlaying = true
        } else if (it.state != PlaybackStateCompat.STATE_NONE && it.state != PlaybackStateCompat.STATE_PLAYING) {

        }  else {
        //    Log.i(TAG, "ddd: none")
        }
    }

    private fun timeToString(progress: Int): String {
        var time = ""
        if (progress <= 9) {
            time = "0:0${progress}"
        } else {
            time = "0:${progress}"
        }
        return time
    }

    private fun setListener(bottomState: BottomSheetBehavior<MotionLayout>) {
        setSeekbarListener()
        binding.button.isPlaying = false // init state
        binding.button.setOnClickListener { lottie ->
            val isPlaying = (lottie as PlayButton).isPlaying
            if (isPlaying) {
                // phat nhac
                // play -> pause icon
                mainActivityViewModel.play()
            } else {
                // pause nhac
                // pause -> play
                mainActivityViewModel.pause()
            }
        }

        motionLayout.setOnClickListener {
            if (bottomState.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomState.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        //  bottomState.setGestureInsetBottomIgnored(true)
        setBottomSheetCallback(bottomState)

        binding.shuffle.setOnClickListener {

            if ((it as ToggleButton).isChecked) {
                mainActivityViewModel.setShuffle(Shuffle.ON.value)
            } else {
                mainActivityViewModel.setShuffle(Shuffle.OFF.value)
            }
        }
        binding.repeat.setOnClickListener {
           when ( (it as RepeatButton).state) {
               RepeatMode.ALL.value -> { mainActivityViewModel.setRepeat(RepeatMode.ALL.value) }
               RepeatMode.ONE.value -> { mainActivityViewModel.setRepeat(RepeatMode.ONE.value) }
               RepeatMode.OFF.value -> { mainActivityViewModel.setRepeat(RepeatMode.OFF.value) }
           }
        }

        binding.next.setOnClickListener {
            mainActivityViewModel.next()
        }
        binding.previ.setOnClickListener {
            mainActivityViewModel.previous()
        }
    }

    private fun setSeekbarListener() {
        binding.trackSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val timeToString = timeToString(progress)
                binding.current.text = timeToString
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mainActivityViewModel.state.removeObserver(observer)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    //seek den vi tri
                    mainActivityViewModel.seekTo(it.progress.toLong())
                    mainActivityViewModel.state.observe(this@MainActivity, observer)
                }
            }
        })
    }

    var oldState = BottomSheetBehavior.STATE_COLLAPSED
    private fun setBottomSheetCallback(bottomState: BottomSheetBehavior<MotionLayout>) {
        bottomState.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= 0 && slideOffset <= 1) {
                    motionLayout.progress = slideOffset
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED && oldState == BottomSheetBehavior.STATE_COLLAPSED) {
                    val animator = ValueAnimator.ofFloat(0.0f, 1f)
                    animator.duration = 200
                    animator.addUpdateListener {
                        bottomMotionLayout.progress = (it.animatedValue as Float)
                    }
                    animator.start()
                    oldState = BottomSheetBehavior.STATE_EXPANDED
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED && oldState == BottomSheetBehavior.STATE_EXPANDED) {
                    val animator = ValueAnimator.ofFloat(1.0f, 0f)
                    animator.duration = 200
                    animator.addUpdateListener {
                        bottomMotionLayout.progress = (it.animatedValue as Float)
                    }
                    animator.start()
                    oldState = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
    }



    private var target = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
            Log.e(TAG, "onBitmapLoaded: fail")
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            bitmap?.let {
                Log.e(TAG, "onBitmapLoaded: da co bitmap")
                motionLayout.setBackground(BitmapDrawable(resources, it))
            }
        }
    }
}