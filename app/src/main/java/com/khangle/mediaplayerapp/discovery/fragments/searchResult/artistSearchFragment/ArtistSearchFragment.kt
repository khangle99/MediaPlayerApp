package com.khangle.mediaplayerapp.discovery.fragments.searchResult.artistSearchFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentArtistSearchBinding
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultViewModel
import com.khangle.mediaplayerapp.recycleviewadapter.ArtistPagingAdapter
import com.khangle.mediaplayerapp.recycleviewadapter.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistSearchFragment : Fragment() {
    lateinit var binding: FragmentArtistSearchBinding
    lateinit var navController: NavController
    private val parenViewmodel: SearchResultViewModel by lazy {
        (requireParentFragment() as SearchResultFragment).viewmodel
    }

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_artist_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    lateinit var artistPagingAdapter: ArtistPagingAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        textView = binding.noResultTV
       navController =  (requireParentFragment() as SearchResultFragment).navController
        recyclerView = binding.artistSearchRecyclerview
        recyclerView.layoutManager = GridLayoutManager(context,2);
        artistPagingAdapter = ArtistPagingAdapter { artist ->
            val bundle = bundleOf("artist" to artist)
            navController.navigate(R.id.action_searchResultFragment_to_artistDetailFragment, bundle)

        }
        recyclerView.adapter = artistPagingAdapter
        recyclerView.addItemDecoration(SpacesItemDecoration(50))
        // chua su dung state adapter
//        val retry = { -> }
//        searchTrackAdapter.withLoadStateFooter(
//            footer = LoadStateAdapter(retry)
//        )
        artistPagingAdapter.addLoadStateListener {
            Log.e("Load state", "onViewCreated: ${ it.append.toString()}")

        }
        lifecycleScope.launch {
            val value = parenViewmodel.searchQuerry.value
            parenViewmodel.querryArtist(value!!).collectLatest {
                artistPagingAdapter.submitData(it)

            }


        }
    }

}