package com.khangle.mediaplayerapp.customview

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView

/**
 * Dang chay thi icon pause, nguoc lai
 *
 */
class PlayButton(context: Context, attributeSet: AttributeSet) :
    LottieAnimationView(context, attributeSet) {

    private val pauseProgress = 0f // va 1f, vi tri luc icon hinh pause
    private val playProgress = 0.5f // vi tri luc icon hinh play
    init {
        progress = playProgress // moi vao se vi tri play (trang thai pause)
    }

    var isPlaying = false
    get() = field
    set(value)  {
        field = value
        if (value) {
            progress = pauseProgress
        } else {
            progress = playProgress
        }
    }

    override fun performClick(): Boolean {
        invalidateState(300)
        super.performClick()
        return true
    }

    private fun invalidateState(duration: Long = 0) {
        if (!isPlaying) { // truong hop dang pause
            // phat nhac
            // play -> pause icon
            val animator = ValueAnimator.ofFloat(playProgress, pauseProgress)
            animator.duration = duration
            animator.addUpdateListener {
                this.setProgress(it.animatedValue as Float)
            }
            isPlaying = !isPlaying // doi thanh play
            animator.start()

        } else {
            // pause nhac
            // pause -> play
            val animator = ValueAnimator.ofFloat(pauseProgress, playProgress)
            animator.duration = duration
            animator.addUpdateListener {
                this.setProgress(it.animatedValue as Float)
            }
            animator.start()
            isPlaying = !isPlaying

        }
    }
}