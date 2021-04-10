package com.khangle.mediaplayerapp.discovery.fragments.PlaylistDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.khangle.mediaplayerapp.BaseFragment
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Playlist
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.databinding.FragmentPlaylistDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistDetailFragment constructor(containerId: Int): BaseFragment() {
    private lateinit var binding: FragmentPlaylistDetailBinding
    private val playListDetailViewmodel: PlaylistDetailViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_playlist_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    lateinit var playlist: Playlist
    lateinit var adapter: PlaylistDetailAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
           playlist =  it.getParcelable("playlist")!!
            binding.playlistTracks.layoutManager = LinearLayoutManager(requireContext())
            adapter = PlaylistDetailAdapter(playlist){
                mainViewModel.play(
                    it.id.toString(),
                    adapter.currentList
                    )
            }
            binding.playlistTracks.adapter = adapter
        }
        playListDetailViewmodel.loadTrack(playlist.id.toString())
        playListDetailViewmodel.artistTrack.observe(viewLifecycleOwner, {
            when (it)
            {
                is Resource.Success -> { adapter.submitList(it.data) ; binding.playlistProgressbar.visibility = View.INVISIBLE }
                is Resource.Loading -> { binding.playlistProgressbar.visibility = View.VISIBLE }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show(); binding.playlistProgressbar.visibility = View.INVISIBLE }
            }
        })

    }

    override fun refresh() {
        playListDetailViewmodel.loadTrack(playlist.id.toString())
    }
}