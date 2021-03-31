package com.khangle.mediaplayerapp.newReleaseDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.chartDetail.BaseFullscreenDialogFragment
import com.khangle.mediaplayerapp.data.model.Album
import com.khangle.mediaplayerapp.databinding.FragmentAlbumDetailBinding
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
    lateinit var adapter: AlbumDetailTrackAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val album: Album = it.getParcelable("album")!!
            viewmodel.loadAlbumTrack(album)
            adapter = AlbumDetailTrackAdapter(album, {
                mainActivityViewModel.play(
                    it.id.toString(),
                    adapter.currentList
                )
            })
        }

        binding.albumTrack.adapter = adapter
        binding.albumTrack.layoutManager = LinearLayoutManager(requireContext())
       viewmodel.albumTracks.observe(viewLifecycleOwner, {

           adapter.submitList(it)
       })
    }


}