package com.khangle.mediaplayerapp.newReleaseDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Album
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.databinding.ItemAlbumDetailHeaderBinding
import com.khangle.mediaplayerapp.recycleviewadapter.TrackDiff
import com.khangle.mediaplayerapp.recycleviewadapter.TrackViewHolder

class AlbumDetailTrackAdapter(val album: Album, val onItemCLick: (Track) -> Unit): ListAdapter<Track, RecyclerView.ViewHolder>(TrackDiff) {
    private val ITEM_TYPE = 0
    private val HEADER_TYPE = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> AlbumHeaderViewHolder.create(parent, {}) // chua can su dung onHeaderClick vao
            else -> TrackViewHolder.create(parent, onItemCLick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrackViewHolder -> {
                val track = getItem(position-1) ?: Track() // tru 1 vi co header
                holder.bind(track)
            }
            is AlbumHeaderViewHolder -> {
                holder.bind(album)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_TYPE
        } else {
            ITEM_TYPE
        }
    }
}

class AlbumHeaderViewHolder(parent: ViewGroup, onItemCLick: (Album) -> Unit): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
    R.layout.item_album_detail_header,parent,false)
) {
    val binding = ItemAlbumDetailHeaderBinding.bind(itemView).also {
        it.root.setOnClickListener {
            onItemCLick(album)
        }
    }
    lateinit var album: Album
    fun bind(album: Album) {
        this.album = album
        binding.album = album
        binding.executePendingBindings()
    }
    companion object {
        fun create(parent: ViewGroup, onItemCLick: (Album) -> Unit): AlbumHeaderViewHolder {
            return AlbumHeaderViewHolder(parent, onItemCLick)
        }
    }
}