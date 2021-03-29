package com.khangle.mediaplayerapp.discovery.fragments.PlaylistDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Playlist
import com.khangle.mediaplayerapp.databinding.FragmentPlaylistDetailBinding
import com.khangle.mediaplayerapp.recycleviewadapter.PlaylistDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistDetailFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistDetailBinding
    private val viewmodel: PlaylistDetailViewModel by viewModels()
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
        viewmodel.loadTrack(playlist.id.toString())
        viewmodel.artistTrack.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

    }

}