package com.khangle.mediaplayerapp.library.fragments.favouriteTrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.BaseFragment
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.databinding.FragmentFavouriteTrackBinding
import com.khangle.mediaplayerapp.recycleviewadapter.TrackAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteTrackFragment : BaseFragment() {
    lateinit var binding: FragmentFavouriteTrackBinding
    private val viewmodel: FavouriteTrackViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        token = requireArguments().getString("token")!!
        viewmodel.fetchFavouriteTracks(token)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_track,container,false)
        return binding.root
    }
    lateinit var adapter: TrackAdapter
    lateinit var recyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.favTrackRecycleview
        adapter = TrackAdapter(parentFragmentManager) {
            mainViewModel.play(
                it.id.toString(),
                adapter.currentList
            )
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        viewmodel.trackList.observe(viewLifecycleOwner, {
            when (it)
            {
                is Resource.Success -> { adapter.submitList(it.data) ; binding.favTrackProgress.visibility = View.INVISIBLE }
                is Resource.Loading -> { binding.favTrackProgress.visibility = View.VISIBLE }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show(); binding.favTrackProgress.visibility = View.INVISIBLE }
            }
        })
    }

    override fun refresh() {
        viewmodel.fetchFavouriteTracks(token)
    }

}