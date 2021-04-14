package com.khangle.mediaplayerapp.discovery.fragments.searchResult.artistSearchFragment

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentArtistSearchBinding
import com.khangle.mediaplayerapp.discovery.fragments.ArtistDetail.ArtistDetailFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultViewModel
import com.khangle.mediaplayerapp.recycleviewadapter.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistSearchFragment : Fragment() {
    lateinit var binding: FragmentArtistSearchBinding
    private val parenViewmodel: SearchResultViewModel by lazy {
        (requireParentFragment() as SearchResultFragment).searchResultViewmodel
    }

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_artist_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    lateinit var artistPagingAdapter: ArtistPagingAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        textView = binding.noResultTV

        recyclerView = binding.artistSearchRecyclerview
        recyclerView.layoutManager = GridLayoutManager(context, 2);
        artistPagingAdapter = ArtistPagingAdapter { artist ->
            val bundle = bundleOf("artist" to artist)
            requireParentFragment().parentFragmentManager.commit {
                setCustomAnimations(R.anim.slide_in,R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                replace(
                    R.id.nav_discovery_fragment,
                    ArtistDetailFragment(R.id.nav_discovery_fragment).also {
                        it.arguments = bundle
                    },
                    "artist_detail"
                )
                addToBackStack(null)
            }

        }
        recyclerView.adapter = artistPagingAdapter
        recyclerView.addItemDecoration(SpacesItemDecoration(50))
        // chua su dung state adapter
//        val retry = { -> }
//        searchTrackAdapter.withLoadStateFooter(
//            footer = LoadStateAdapter(retry)
//        )
        artistPagingAdapter.addLoadStateListener {
            Log.e("Load state", "onViewCreated: ${it.append.toString()}")

        }
        lifecycleScope.launch {
            val value = parenViewmodel.searchQuerry.value
            parenViewmodel.querryArtist(value!!)?.collectLatest {
                artistPagingAdapter.submitData(it)
            }
        }
        artistPagingAdapter.addLoadStateListener {
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