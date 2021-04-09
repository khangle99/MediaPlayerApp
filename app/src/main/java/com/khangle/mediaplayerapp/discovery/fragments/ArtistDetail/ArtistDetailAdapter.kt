package com.khangle.mediaplayerapp.discovery.fragments.ArtistDetail

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.artistSearchFragment.ArtistAdapter
import com.khangle.mediaplayerapp.recycleviewadapter.*

class ArtistDetailAdapter(val artist: Artist, val artistAdapter: ArtistAdapter, val onItemClick: (Track) -> Unit) :
    ListAdapter<Track, RecyclerView.ViewHolder>(TrackDiff) {
    private val ITEM_TYPE = 0
    private val HEADER_TYPE = 1
    private val FOOTER_TYPE = 2
    val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> ArtistHeaderViewHolder.create(parent, {}) // chua truyen bien onHeaderClick vao
            FOOTER_TYPE -> ArtistFooterViewHolder.create(parent, onItemClick)
            else -> TrackViewHolder.create(parent, onItemClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrackViewHolder -> {
                val track = getItem(position-1) ?: Track() // tru 1 vi co header
                holder.bind(track)
            }
            is ArtistHeaderViewHolder -> {
                holder.bind(artist)
            }
            is ArtistFooterViewHolder -> {
                // setup recyclerview
                holder.setupRecycler(artistAdapter,viewPool)
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_TYPE
        } else if (position == currentList.size - 1) {
            FOOTER_TYPE
        } else {
            ITEM_TYPE
        }
    }
}