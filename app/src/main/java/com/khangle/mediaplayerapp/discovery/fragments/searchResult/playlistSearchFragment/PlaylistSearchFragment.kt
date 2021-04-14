package com.khangle.mediaplayerapp.discovery.fragments.searchResult.playlistSearchFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentPlaylistSearchBinding
import com.khangle.mediaplayerapp.discovery.fragments.PlaylistDetail.PlaylistDetailFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaylistSearchFragment : Fragment() {
    lateinit var binding: FragmentPlaylistSearchBinding
    private val parentViewModel: SearchResultViewModel by lazy {
        (requireParentFragment() as SearchResultFragment).searchResultViewmodel
    }
    lateinit var recyclerView: RecyclerView
    lateinit var playlistAdapter: PlaylistPagingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_playlist_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.albumSearchRecycleview
        playlistAdapter = PlaylistPagingAdapter { playlist ->
            val bundle = bundleOf("playlist" to playlist)
            requireParentFragment().parentFragmentManager.commit {
                setCustomAnimations(R.anim.slide_in,R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                replace(
                    R.id.nav_discovery_fragment,
                    PlaylistDetailFragment(R.id.nav_discovery_fragment).also {
                        it.arguments = bundle
                    },
                    "playlist_detail"
                )
                addToBackStack("")
            }
        }
        recyclerView.adapter = playlistAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val value = parentViewModel.searchQuerry.value
            parentViewModel.querryPlaylist(value!!)?.collectLatest {
                playlistAdapter.submitData(it)
            }


        }
        playlistAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        (it.refresh as LoadState.Error).error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}