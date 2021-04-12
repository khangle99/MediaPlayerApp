package com.khangle.mediaplayerapp.home.fragments.albumDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Album
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.databinding.FragmentAlbumDetailBinding
import com.khangle.mediaplayerapp.home.fragments.chartDetail.BaseFullscreenDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumDetailFragment : BaseFullscreenDialogFragment() {

    lateinit var binding: FragmentAlbumDetailBinding
    private val viewmodel: AlbumDetailViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_album_detail, container, false)
        return binding.root
    }
    lateinit var  album: Album
    override fun refresh() {
        viewmodel.loadAlbumTrack(album)
    }

    lateinit var adapter: AlbumDetailTrackAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            album= it.getParcelable("album")!!
            viewmodel.loadAlbumTrack(album)
            adapter = AlbumDetailTrackAdapter(parentFragmentManager,album) {
                mainActivityViewModel.play(
                    it.id.toString(),
                    adapter.currentList
                )
            }
        }

        binding.albumTrack.adapter = adapter
        binding.albumTrack.layoutManager = LinearLayoutManager(requireContext())
       viewmodel.albumTracks.observe(viewLifecycleOwner, {

           when (it)
           {
               is Resource.Success -> { adapter.submitList(it.data) ; binding.albumDetailProgress.visibility = View.INVISIBLE }
               is Resource.Loading -> { binding.albumDetailProgress.visibility = View.VISIBLE }
               is Resource.Error -> {
                   Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show(); binding.albumDetailProgress.visibility = View.INVISIBLE }
           }
       })
    }


}