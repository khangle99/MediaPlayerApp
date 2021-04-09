package com.khangle.mediaplayerapp.discovery.fragments.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.data.model.Genre
import com.khangle.mediaplayerapp.databinding.ItemGenreBinding

class GenreAdapter(val onItemClick: (Genre) -> Unit): ListAdapter<Genre, GenreAdapter.GenreItemViewholder>(Genre.diffUtil) {


    class GenreItemViewholder(val binding: ItemGenreBinding, onItemClick: (Genre) -> Unit ): RecyclerView.ViewHolder(binding.root) {
        lateinit var genre: Genre
        init {
            binding.root.setOnClickListener {
                onItemClick(genre)
            }
        }
        fun bind(genre: Genre) {
            this.genre = genre
            binding.genre = genre
            binding.executePendingBindings()
        }
        companion object {
            fun create(parent: ViewGroup, onItemClick: (Genre) -> Unit): GenreItemViewholder {
                val inflater = LayoutInflater.from(parent.context)
                val bind = ItemGenreBinding.inflate(inflater,parent,false)
               //  bind.setLifecycleOwner(bind.) chua can set do k lang nghe livedata
                return GenreItemViewholder(bind, onItemClick)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreItemViewholder {
       return GenreItemViewholder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: GenreItemViewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}