package com.khangle.mediaplayerapp.recycleviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.data.model.ArtistDiff
import com.khangle.mediaplayerapp.databinding.ItemArtistBinding

class ArtistPagingAdapter(private val onItemClick: (Artist) -> Unit): PagingDataAdapter<Artist, ArtistViewHolder>(
    ArtistDiff
) {
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        return ArtistViewHolder.create(parent, onItemClick)
    }

}

class ArtistAdapter(private val onItemClick: (Artist) -> Unit): ListAdapter<Artist, ArtistViewHolder>(
    ArtistDiff
) {
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        return ArtistViewHolder.create(parent, onItemClick)
    }

}

class ArtistViewHolder(parent: ViewGroup,val onItemClick: (Artist) -> Unit): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
    .inflate(R.layout.item_artist, parent, false)) {

    lateinit var artist: Artist
    val binding = ItemArtistBinding.bind(itemView).also {
        it.root.setOnClickListener {
            onItemClick(artist)
        }
    }

    fun bind(artist: Artist?) {
        if (artist!= null) {
            this.artist = artist
            binding.artistShimmer.stopShimmer()
            binding.artistShimmer.hideShimmer()
            binding.artist = artist
            binding.executePendingBindings()
        } else {
            binding.artistShimmer.showShimmer(true)
        }
    }
    companion object {
        fun create(parent: ViewGroup, onItemClick: (Artist) -> Unit): ArtistViewHolder {
            return ArtistViewHolder(parent,onItemClick)
        }
    }

}