package com.khangle.mediaplayerapp.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.BaseFragment
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.chartDetail.ChartDetailDialogFragment
import com.khangle.mediaplayerapp.databinding.FragmentHomeBinding
import com.khangle.mediaplayerapp.discovery.fragments.catalog.GenreAdapter
import com.khangle.mediaplayerapp.home.adapter.ChartAlbumAdapter
import com.khangle.mediaplayerapp.newReleaseDetail.AlbumDetailFragment
import com.khangle.mediaplayerapp.recycleviewadapter.TrackAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val TAG = "HomeFragment"
    private val homeViewModel: HomeViewModel by viewModels()
    private val mainViewmodel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var suggestionTrackAdapter: TrackAdapter
    private lateinit var newReleaseAlbumAdapter: ChartAlbumAdapter
    private lateinit var chartGenreAdapter: GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun refresh() {
        homeViewModel.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "da chay onCreate")
        homeViewModel.refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: call onViewCreated---")
        newReleaseAlbumAdapter = ChartAlbumAdapter {
            val albumDetailFragment = AlbumDetailFragment()
            albumDetailFragment.arguments = bundleOf("album" to it)
            albumDetailFragment.show(parentFragmentManager, "albumDetail")
        }
        binding.newReleaseAlbums.adapter = newReleaseAlbumAdapter
        binding.newReleaseAlbums.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )
        chartGenreAdapter = GenreAdapter { genre ->
            val chartDetailDialogFragment = ChartDetailDialogFragment()
            val bundle = bundleOf("genre" to genre)
            chartDetailDialogFragment.arguments = bundle
            chartDetailDialogFragment.show(parentFragmentManager, "chartDetail")
        }
        binding.chartGenre.adapter = chartGenreAdapter
        binding.chartGenre.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )
        suggestionTrackAdapter = TrackAdapter(onItemClick = { track ->
            Log.i(TAG, "onViewCreated: click")
            mainViewmodel.play(
                track.id.toString(),
                homeViewModel.suggestionTracks.value!!
            ) // !! la vi phai co item moi click dc, sure
        })
        binding.tracksChart.layoutManager = LinearLayoutManager(requireContext())
        binding.tracksChart.adapter = suggestionTrackAdapter
        binding.tracksChart.isNestedScrollingEnabled = false
        homeViewModel.suggestionTracks.observe(requireActivity(), Observer {
            if (it.size > 0) {
                suggestionTrackAdapter.submitList(it)
                binding.swipeRefresh.isRefreshing = false
            }
        })

        homeViewModel.genreList.observe(viewLifecycleOwner, {
            chartGenreAdapter.submitList(it)
        })
        homeViewModel.newReleaseAlbums.observe(viewLifecycleOwner, {
            newReleaseAlbumAdapter.submitList(it)
        })

        binding.swipeRefresh.setOnRefreshListener {
            homeViewModel.refresh()
        }
        homeViewModel.error.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        homeViewModel.isLoading.observe(viewLifecycleOwner, {
            binding.homeProgress.isVisible = it
        })

    }



}

const val PLAYLISTID = "PLAYLISTID"

enum class PlaylistId(val value: String) {
    CHART("chart")
}
