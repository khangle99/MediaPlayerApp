package com.khangle.mediaplayerapp.library.fragments.favouriteArtist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.BaseFragment
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.databinding.FragmentFavouriteArtistBinding
import com.khangle.mediaplayerapp.discovery.fragments.ArtistDetail.ArtistDetailFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.artistSearchFragment.ArtistAdapter
import com.khangle.mediaplayerapp.recycleviewadapter.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteArtistFragment : BaseFragment() {

    private val viewmode: FavouriteArtistViewModel by viewModels()
    lateinit var binding: FragmentFavouriteArtistBinding
    lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        token = requireArguments().getString("token")!!
        viewmode.fetchArtist(token)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_artist, container, false)
        return binding.root
    }

    lateinit var adapter: ArtistAdapter
    lateinit var recyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArtistAdapter { artist ->
            val bundle = bundleOf("artist" to artist)
           parentFragmentManager.commit {
               setReorderingAllowed(true)
                replace(
                    R.id.libraryHost,
                    ArtistDetailFragment(R.id.libraryHost).also {
                        it.arguments = bundle
                    }
                )
                addToBackStack(null)
            }
        }
        recyclerView = binding.favArtistRecycleview
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(SpacesItemDecoration(50))
        recyclerView.layoutManager = GridLayoutManager(context, 2);
        viewmode.artistList.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    binding.favArtistProgress.visibility = View.INVISIBLE
                }
                is Resource.Loading -> {
                    binding.favArtistProgress.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.count = "Artist Count: ${it.data!!.size}"
                    binding.header = "Favourite Artists"
                    binding.executePendingBindings()
                    adapter.submitList(it.data)
                    binding.favArtistProgress.visibility = View.INVISIBLE

                }
            }
        })
    }

    override fun refresh() {
        viewmode.fetchArtist(token)
    }


}