package com.khangle.mediaplayerapp.discovery.fragments.ArtistDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.BaseFragment
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.databinding.FragmentArtistDetailBinding
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.artistSearchFragment.ArtistAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailFragment constructor(val containerId: Int): BaseFragment() { // su dung 2 noi sau khi click search va click vao favourtite artist
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    lateinit var binding: FragmentArtistDetailBinding
    private val artistDetailViewmodel: ArtistDetailViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by viewModels()
    lateinit var artist: Artist
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding =  DataBindingUtil.inflate(
           inflater,
           R.layout.fragment_artist_detail,
           container,
           false
       )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    lateinit var artistTrackRecyclerView: RecyclerView
    lateinit var artistArtistAdapter: ArtistDetailAdapter
    lateinit var artistAdapter: ArtistAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            artist = it.getParcelable("artist")!! // co the crash neu sai key
        }

        artistDetailViewmodel.loadTrackAndRelateArtist(artist.id.toString())

        artistTrackRecyclerView = binding.artistTrackList
        // nested ==> refactor
        artistAdapter = ArtistAdapter{ artist ->
            val bundle = bundleOf("artist" to artist)
            parentFragmentManager.commit {
                setCustomAnimations(R.anim.slide_in,R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                replace(
                    containerId,
                    ArtistDetailFragment(containerId).also { it.arguments = bundle },
                    "relate"
                )
                addToBackStack(null)
            }
        }
        artistArtistAdapter = ArtistDetailAdapter(parentFragmentManager,artist, artistAdapter){ track ->
            mainViewModel.play(
                track,
                artistArtistAdapter.currentList
            )
        }
        artistTrackRecyclerView.adapter = artistArtistAdapter
        artistTrackRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        artistDetailViewmodel.artistTrack.observe(viewLifecycleOwner, {
            artistArtistAdapter.submitList(it)
        })
        artistDetailViewmodel.relateArtist.observe(viewLifecycleOwner, {
            artistAdapter.submitList(it)
        })
        artistDetailViewmodel.error.observe(viewLifecycleOwner, {
            if (!it.equals("")) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        artistDetailViewmodel.isLoading.observe(viewLifecycleOwner, {
            binding.artistProgress.isVisible = it
        })
    }

    override fun refresh() {
        artistDetailViewmodel.loadTrackAndRelateArtist(artist.id.toString())
    }

}