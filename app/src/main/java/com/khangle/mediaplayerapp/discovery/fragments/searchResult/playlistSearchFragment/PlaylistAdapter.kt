package com.khangle.mediaplayerapp.discovery.fragments.searchResult.playlistSearchFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Playlist
import com.khangle.mediaplayerapp.data.model.PlaylistDiff
import com.khangle.mediaplayerapp.databinding.ItemPlaylistBinding

class PlaylistAdapter(val onClick: (Playlist) -> Unit) : ListAdapter<Playlist, PlaylistViewHolder>(PlaylistDiff){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.create(parent, onClick)
    }
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PlaylistPagingAdapter(val onClick: (Playlist) -> Unit) : PagingDataAdapter<Playlist, PlaylistViewHolder>(PlaylistDiff){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PlaylistViewHolder(parent: ViewGroup, val onClick: (Playlist) -> Unit): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
) {
    val binding = ItemPlaylistBinding.bind(itemView).also {
        it.root.setOnClickListener {
            onClick(playlist)
        }
    }
    lateinit var playlist: Playlist
    fun bind(playlist: Playlist?) {
        if (playlist != null) {
            binding.albumShimmer.stopShimmer()
            binding.albumShimmer.hideShimmer()
            this.playlist = playlist
            binding.playlist = playlist
        } else {
            binding.albumShimmer.showShimmer(true)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onClick: (Playlist) -> Unit) : PlaylistViewHolder {
            return PlaylistViewHolder(parent, onClick)
        }
    }

}