package com.khangle.mediaplayerapp.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.data.model.Album
import com.khangle.mediaplayerapp.data.model.AlbumDiff
import com.khangle.mediaplayerapp.databinding.ItemAlbumBinding

class ChartAlbumAdapter(val itemClick: (Album) -> Unit): ListAdapter<Album, AlbumViewHolder>(AlbumDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder.create(parent,itemClick)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class AlbumViewHolder(val binding: ItemAlbumBinding, itemClick: (Album) -> Unit): RecyclerView.ViewHolder(binding.root) {
    lateinit var album: Album
    init {
        binding.root.setOnClickListener {
            itemClick(album)
        }
    }
    fun bind(album: Album) {
        this.album = album
        binding.album = album
        binding.executePendingBindings()
    }
    companion object {
        fun create(parent: ViewGroup, itemClick: (Album) -> Unit): AlbumViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemAlbumBinding.inflate(inflater, parent,false)
            return AlbumViewHolder(binding, itemClick)
        }
    }
}