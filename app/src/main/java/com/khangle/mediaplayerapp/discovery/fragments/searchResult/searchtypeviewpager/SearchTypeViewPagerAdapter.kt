package com.khangle.mediaplayerapp.discovery.fragments.searchResult.searchtypeviewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.artistSearchFragment.ArtistSearchFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.playlistSearchFragment.PlaylistSearchFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.trackSearchFragment.TrackSearchFragment

class SearchTypeViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
      return when (position) {
           0 -> {
               TrackSearchFragment()
           }
           1 -> {
                ArtistSearchFragment()
           }
           2 -> {
                PlaylistSearchFragment()
           }
           else -> Fragment()
       }
    }
}