package com.khangle.mediaplayerapp.discovery.fragments.searchResult.trackSearchFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentTrackSearchBinding
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultViewModel
import com.khangle.mediaplayerapp.recycleviewadapter.TrackPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrackSearchFragment() : Fragment() {
    lateinit var binding: FragmentTrackSearchBinding
    private val mainViewModel: MainActivityViewModel by viewModels()
    private val parenViewmodel: SearchResultViewModel by lazy {
        (requireParentFragment() as SearchResultFragment).viewmodel
    }
    lateinit var searchTrackAdapter: TrackPagingAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var textView: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_track_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        textView = binding.noResultTV
        recyclerView = binding.trackSearchRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(context);
         searchTrackAdapter = TrackPagingAdapter {
             mainViewModel.play(
                 it.id.toString(),
                 searchTrackAdapter.snapshot().items
             )
        }
        recyclerView.adapter = searchTrackAdapter
        // chua su dung state adapter
//        val retry = { -> }
//        searchTrackAdapter.withLoadStateFooter(
//            footer = LoadStateAdapter(retry)
//        )
        searchTrackAdapter.addLoadStateListener {
            Log.e("Load state", "onViewCreated: ${ it.append.toString()}")

        }
        lifecycleScope.launch {
            val value = parenViewmodel.searchQuerry.value
            parenViewmodel.querryTrack(value!!).collectLatest {
                searchTrackAdapter.submitData(it)

            }


        }
    }


}