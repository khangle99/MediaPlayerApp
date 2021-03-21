package com.khangle.mediaplayerapp.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentHomeBinding
import com.khangle.mediaplayerapp.home.trackchart.TrackChartAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private  val TAG = "HomeFragment"
    private val homeViewModel: HomeViewModel by viewModels()
    private val mainViewmodel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var chartTrackAdapter: TrackChartAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chartTrackAdapter = TrackChartAdapter(onItemClick = { track -> Log.i(TAG, "onViewCreated: click")

            val bundle = Bundle().apply {
                putString(PLAYLISTID, PlaylistId.CHART.value)
            }

            mainViewmodel.play(track,homeViewModel.chartTracks.value!!, bundle) // !! la vi phai co item moi click dc, sure
        })
        binding.tracksChart.layoutManager = LinearLayoutManager(requireContext())
        binding.tracksChart.adapter = chartTrackAdapter
        homeViewModel.chartTracks.observe(requireActivity(), Observer {
            chartTrackAdapter.submitList(it)
        })

    }

}
const val PLAYLISTID = "PLAYLISTID"
enum class PlaylistId(val value: String) {
    CHART("chart")
}
