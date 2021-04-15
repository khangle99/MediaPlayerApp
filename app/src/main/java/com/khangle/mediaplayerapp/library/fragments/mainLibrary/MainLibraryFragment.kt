package com.khangle.mediaplayerapp.library.fragments.mainLibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.mediaplayerapp.*
import com.khangle.mediaplayerapp.databinding.FragmentMainLibraryBinding
import com.khangle.mediaplayerapp.discovery.fragments.ArtistDetail.ArtistDetailFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.artistSearchFragment.ArtistAdapter
import com.khangle.mediaplayerapp.library.LibraryViewModel
import com.khangle.mediaplayerapp.library.fragments.favouriteArtist.FavouriteArtistFragment
import com.khangle.mediaplayerapp.library.fragments.favouriteTrack.FavouriteTrackFragment
import com.khangle.mediaplayerapp.recycleviewadapter.TrackAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainLibraryFragment : BaseFragment() {

    lateinit var binding: FragmentMainLibraryBinding
    lateinit var token: String
    private val mainLibraryViewModel: MainLibraryViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val libraryViewModel: LibraryViewModel by viewModels(ownerProducer = { requireParentFragment()} )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        token = requireArguments().getString("token")!!
        mainLibraryViewModel.fetchRecommend(token)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main_library, container, false)
        return binding.root
    }
    lateinit var recommendTrackAdapter: TrackAdapter
    lateinit var recommendArtistAdapter: ArtistAdapter
    lateinit var recommendTrackRecyclerView: RecyclerView
    lateinit var recommendArtistRecyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickEvent()
        setupRecycleviews()
        mainLibraryViewModel.status.observe(viewLifecycleOwner, {

        })
        mainLibraryViewModel.recommendArtists.observe(viewLifecycleOwner, {
            recommendArtistAdapter.submitList(it)
        })

        mainLibraryViewModel.recommendTracks.observe(viewLifecycleOwner, {
            recommendTrackAdapter.submitList(it)
        })

        libraryViewModel.user.observe(viewLifecycleOwner, {
            // setup UI user info
            binding.userInfo = it
        })
    }

    private fun setupRecycleviews() {
        recommendArtistRecyclerView = binding.recommendArtist
        recommendTrackRecyclerView = binding.recommentTrack
        recommendTrackAdapter = TrackAdapter(parentFragmentManager) {
            mainActivityViewModel.play(
                it,
                recommendTrackAdapter.currentList
            )
        }
        recommendArtistAdapter = ArtistAdapter { artist ->
            val bundle = bundleOf("artist" to artist)
            parentFragmentManager.commit {
                setCustomAnimations(R.anim.slide_in,R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
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

        recommendArtistRecyclerView.adapter = recommendArtistAdapter
        recommendArtistRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recommendTrackRecyclerView.adapter = recommendTrackAdapter
        recommendTrackRecyclerView.isNestedScrollingEnabled = false
        recommendTrackRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupClickEvent() {
        binding.favouriteTrack.setOnClickListener { // ham test
            parentFragmentManager.commit {
                setCustomAnimations(R.anim.slide_in,R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                setReorderingAllowed(true)
                val favouriteTrackFragment = FavouriteTrackFragment()
                favouriteTrackFragment.arguments = bundleOf("token" to token)
                replace(R.id.libraryHost, favouriteTrackFragment)
                addToBackStack(null)
            }
        }

        binding.signOutBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                requireContext().dataStore.edit { settings ->
                    settings[TOKEN] = ""
                }
            }
        }

        binding.favouriteArtist.setOnClickListener {
            parentFragmentManager.commit {
                setCustomAnimations(R.anim.slide_in,R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                setReorderingAllowed(true)
                val favouriteArtistFragment = FavouriteArtistFragment()
                favouriteArtistFragment.arguments = bundleOf("token" to token)
                replace(R.id.libraryHost, favouriteArtistFragment)
                addToBackStack(null)
            }
        }
    }
    override fun refresh() {
        mainLibraryViewModel.fetchRecommend(token)
    }

}