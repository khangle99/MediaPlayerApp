package com.khangle.mediaplayerapp.chartDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Genre
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.databinding.ItemChartHeaderBinding
import com.khangle.mediaplayerapp.recycleviewadapter.TrackDiff
import com.khangle.mediaplayerapp.recycleviewadapter.TrackViewHolder

class ChartDetailAdapter(val genre: Genre,val onItemClick: (Track) -> Unit): ListAdapter<Track, RecyclerView.ViewHolder>(TrackDiff) {
    private val ITEM_TYPE = 0
    private val HEADER_TYPE = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> ChartDetailHeaderViewHolder.create(parent, {}) // chua can su dung onHeaderClick vao
            else -> TrackViewHolder.create(parent, onItemClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TrackViewHolder -> {
                val track = getItem(position-1) ?: Track() // tru 1 vi co header
                holder.bind(track)
            }
            is ChartDetailHeaderViewHolder -> {
                holder.bind(genre)
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

class ChartDetailHeaderViewHolder(parent: ViewGroup, onItemClick: (Genre) -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_chart_header, parent, false)
) {
    val binding = ItemChartHeaderBinding.bind(itemView).also {
        it.root.setOnClickListener {
            onItemClick(genre)
        }
    }
    fun bind(genre: Genre) {
        binding.genre = genre
        this.genre = genre
        binding.executePendingBindings()
    }
    lateinit var genre: Genre
    companion object {

        fun create(parent: ViewGroup, onItemClick: (Genre) -> Unit): ChartDetailHeaderViewHolder {
            return ChartDetailHeaderViewHolder(parent, onItemClick)
        }
    }

}