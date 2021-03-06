package com.khangle.mediaplayerapp.discovery.fragments.PlaylistDetail

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.data.model.Playlist
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.recycleviewadapter.PlaylistHeaderViewHolder
import com.khangle.mediaplayerapp.recycleviewadapter.TrackDiff
import com.khangle.mediaplayerapp.recycleviewadapter.TrackViewHolder

class PlaylistDetailAdapter(val fragmentManager: FragmentManager,val playlist: Playlist, val onItemClick: (Track) -> Unit) :
    ListAdapter<Track, RecyclerView.ViewHolder>(TrackDiff) {
    private val ITEM_TYPE = 0
    private val HEADER_TYPE = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> PlaylistHeaderViewHolder.create(parent, {}) // chua can su dung onHeaderClick vao
            else -> TrackViewHolder.create(fragmentManager,parent, onItemClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrackViewHolder -> {
                val track = getItem(position-1) ?: Track() // tru 1 vi co header
                holder.bind(track)
            }
            is PlaylistHeaderViewHolder -> {
                holder.bind(playlist)
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