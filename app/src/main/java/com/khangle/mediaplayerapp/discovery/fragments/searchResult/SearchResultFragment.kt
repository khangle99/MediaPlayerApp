package com.khangle.mediaplayerapp.discovery.fragments.searchResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentSearchResultBinding
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.searchtypeviewpager.SearchTypeViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment: Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    val searchResultViewmodel: SearchResultViewModel by viewModels()
    private val mainViewModel: MainActivityViewModel by viewModels()
    lateinit var navController: NavController
    private lateinit var viewPagerAdapter: SearchTypeViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        val querry = arguments?.getString("query")!!
        searchResultViewmodel.searchQuerry.value = querry
        viewPager = binding.pager
        viewPagerAdapter = SearchTypeViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Track"
                }
                1 -> {
                    tab.text = "Artist"
                }
                2 -> {
                    tab.text = "Playlist"
                }
            }

        }.attach()

    }

}