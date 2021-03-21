package com.khangle.mediaplayerapp.customview


import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageButton
import com.khangle.mediaplayerapp.R


class RepeatButton(context: Context, attributeSet: AttributeSet): ImageButton(
    context,
    attributeSet
) {
    private val MAX_STATES = 3
    var state = 0
    var srcRepeatOff: Drawable? = null
    var srcRepeatOne: Drawable? = null
    var srcRepeatAll: Drawable? = null
  //  var repeatState = 0
    init {
        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.RepeatButton,
            0, 0
        )

        try {
            state = a
                .getInteger(R.styleable.RepeatButton_repeat_state, 0)
            srcRepeatOff = a
                .getDrawable(R.styleable.RepeatButton_src_repeat_off)
            srcRepeatOne = a
                .getDrawable(R.styleable.RepeatButton_src_repeat_one)
            srcRepeatAll = a
                .getDrawable(R.styleable.RepeatButton_src_repeat_all)
        } catch (e: Exception) {
        } finally {
            a.recycle()
        }

        when (state) {
            0 -> this.setBackground(srcRepeatOff)
            1 -> this.setBackground(srcRepeatAll)
            2 -> this.setBackground(srcRepeatOne)
            else -> {
            }
        }

    }

    override fun performClick(): Boolean {
        nextState()
        setStateBackground()
        super.performClick()

        return true
    }
    private fun nextState() {
        state++
        if (state === MAX_STATES) {
            state = 0
        }
    }
    private fun setStateBackground() {
        when (state) {
            0 -> {
                this.background = srcRepeatOff
            }
            1 -> {
                this.background = srcRepeatAll
            }
            2 -> {
                this.background = srcRepeatOne
            }
            else -> {
            }
        }
    }

}