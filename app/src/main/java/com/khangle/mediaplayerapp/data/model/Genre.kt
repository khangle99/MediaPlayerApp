package com.khangle.mediaplayerapp.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class Genre(
    val id: Int = 0,
    val name: String = "",
    @SerializedName("picture_medium")
    val pictureMedium: String = "",
    @SerializedName("picture_big")
    val pictureBig: String = ""
) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Genre>() {
            override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
               return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}

