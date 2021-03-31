package com.khangle.mediaplayerapp.discovery.fragments.searchResult.playlistSearchFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentPlaylistSearchBinding
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultViewModel
import com.khangle.mediaplayerapp.recycleviewadapter.PlaylistPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaylistSearchFragment : Fragment() {

    lateinit var binding: FragmentPlaylistSearchBinding
    private val parentViewModel: SearchResultViewModel by lazy {
        (requireParentFragment() as SearchResultFragment).viewmodel
    }
    lateinit var navController: NavController
    lateinit var recyclerView: RecyclerView
    lateinit var playlistAdapter: PlaylistPagingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_playlist_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController =  (requireParentFragment() as SearchResultFragment).navController
        recyclerView = binding.albumSearchRecycleview
        playlistAdapter = PlaylistPagingAdapter { playlist ->
            val bundle = bundleOf("playlist" to playlist)
            navController.navigate(R.id.action_searchResultFragment_to_playlistDetailFragment, bundle)
        }
        recyclerView.adapter = playlistAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val value = parentViewModel.searchQuerry.value
            parentViewModel.querryPlaylist(value!!).collectLatest {
                playlistAdapter.submitData(it)
            }


        }
    }

}