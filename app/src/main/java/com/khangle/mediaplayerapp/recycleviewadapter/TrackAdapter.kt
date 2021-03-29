package com.khangle.mediaplayerapp.recycleviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.data.model.Playlist
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.databinding.ItemArtistFooterBinding
import com.khangle.mediaplayerapp.databinding.ItemArtistHeaderBinding
import com.khangle.mediaplayerapp.databinding.ItemPlaylistHeaderBinding
import com.khangle.mediaplayerapp.databinding.ItemTrackBinding

class TrackAdapter(val onItemClick: (Track) -> Unit) :
    ListAdapter<Track, TrackViewHolder>(TrackDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position) ?: Track()
        holder.bind(track)
    }
}

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

class PlaylistDetailAdapter(val playlist: Playlist, val onItemClick: (Track) -> Unit) :
    ListAdapter<Track, RecyclerView.ViewHolder>(TrackDiff) {
    private val ITEM_TYPE = 0
    private val HEADER_TYPE = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> PlaylistHeaderViewHolder.create(parent, {}) // chua can su dung onHeaderClick vao
            else -> TrackViewHolder.create(parent, onItemClick)
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

class TrackPagingAdapter(val onItemClick: (Track) -> Unit) :
    PagingDataAdapter<Track, TrackViewHolder>(
        TrackDiff
    ) {
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder.create(parent, onItemClick)
    }
}

class TrackViewHolder(private val binding: ItemTrackBinding, onItemClick: (Track) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var track: Track
    init {
        binding.root.setOnClickListener {
            onItemClick(track)
        }
    }

    fun bind(track: Track?) {
        if (track != null) {
            binding.trackShimmer.stopShimmer()
            binding.trackShimmer.hideShimmer()
            this.track = track
            binding.track = track
            binding.executePendingBindings() // cap nhat data
        } else {
            binding.artistName.text = "Loading"
            binding.imageView.setImageResource(R.drawable.ic_app)
            binding.trackShimmer.showShimmer(true);
        }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Track) -> Unit): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val bind = ItemTrackBinding.inflate(inflater, parent, false)
            // bind.setLifecycleOwner(parent.findViewTreeLifecycleOwner())
            return TrackViewHolder(bind, onItemClick)
        }
    }
}

class ArtistHeaderViewHolder(private val binding: ItemArtistHeaderBinding, onItemClick: (Artist) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var artist: Artist
    init {
        binding.root.setOnClickListener {
            onItemClick(artist)
        }
    }

    fun bind(artist: Artist) {
        this.artist = artist
        binding.artist = artist
        binding.executePendingBindings() // cap nhat data
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Artist) -> Unit): ArtistHeaderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val bind = ItemArtistHeaderBinding.inflate(inflater, parent, false)
            // bind.setLifecycleOwner(parent.findViewTreeLifecycleOwner())
            return ArtistHeaderViewHolder(bind, onItemClick)
        }
    }

}
class PlaylistHeaderViewHolder(private val binding: ItemPlaylistHeaderBinding, onItemClick: (Playlist) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var playlist: Playlist
    init {
        binding.root.setOnClickListener {
            onItemClick(playlist)
        }
    }

    fun bind(playlist: Playlist) {
        this.playlist = playlist
        binding.playlist = playlist
        binding.executePendingBindings() // cap nhat data
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Playlist) -> Unit): PlaylistHeaderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val bind = ItemPlaylistHeaderBinding.inflate(inflater, parent, false)
            // bind.setLifecycleOwner(parent.findViewTreeLifecycleOwner())
            return PlaylistHeaderViewHolder(bind, onItemClick)
        }
    }

}
class ArtistFooterViewHolder(private val binding: ItemArtistFooterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun setupRecycler(adapter: ArtistAdapter, viewPool: RecyclerView.RecycledViewPool) {
        binding.relateArtist.layoutManager = LinearLayoutManager(binding.root.context,RecyclerView.HORIZONTAL,false)
        binding.relateArtist.adapter = adapter
        binding.relateArtist.setRecycledViewPool(viewPool)
        binding.relateArtist.setHasFixedSize(true)
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (Track) -> Unit): ArtistFooterViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val bind = ItemArtistFooterBinding.inflate(inflater, parent, false)
            // bind.setLifecycleOwner(parent.findViewTreeLifecycleOwner())
            return ArtistFooterViewHolder(bind)
        }
    }

}

object TrackDiff : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.id == newItem.id // do content k doi nen k sao
    }

}