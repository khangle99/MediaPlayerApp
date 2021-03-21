package com.khangle.mediaplayerapp.home.trackchart

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.databinding.ItemTrackBinding

class TrackChartAdapter(val onItemClick: (MediaBrowserCompat.MediaItem) -> Unit) : ListAdapter<MediaBrowserCompat.MediaItem, TrackChartAdapter.TrackViewHolder>(MyDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position) ?: MediaBrowserCompat.MediaItem(MediaDescriptionCompat.fromMediaDescription(Any()), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE)
        holder.bind(track)
    }

    class TrackViewHolder(private val binding: ItemTrackBinding, onItemClick: (MediaBrowserCompat.MediaItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var track: MediaBrowserCompat.MediaItem
        init {

            binding.root.setOnClickListener {
                onItemClick(track)
            }
        }
        fun bind(track: MediaBrowserCompat.MediaItem) {
            this.track = track
            binding.track = track
            binding.executePendingBindings() // cap nhat data

        }
        companion object {
            fun create(parent: ViewGroup, onItemClick: (MediaBrowserCompat.MediaItem) -> Unit): TrackViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val bind = ItemTrackBinding.inflate(inflater,parent,false)
               // bind.setLifecycleOwner(parent.findViewTreeLifecycleOwner())
                return TrackViewHolder(bind, onItemClick)
            }
        }

    }
}

object MyDiff: DiffUtil.ItemCallback<MediaBrowserCompat.MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaBrowserCompat.MediaItem, newItem: MediaBrowserCompat.MediaItem): Boolean {
        return oldItem.mediaId == newItem.mediaId
    }

    override fun areContentsTheSame(oldItem: MediaBrowserCompat.MediaItem, newItem: MediaBrowserCompat.MediaItem): Boolean {
        return oldItem.mediaId == newItem.mediaId
    }

}