package com.khangle.mediaplayerapp.library

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.khangle.mediaplayerapp.*
import com.khangle.mediaplayerapp.databinding.FragmentLibraryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LibraryFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentLibraryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_library, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    val stringExtra = intent?.getStringExtra("token")
                    Toast.makeText(
                        requireContext(),
                        intent?.getStringExtra("token"),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        binding.loginBtn.setOnClickListener {
            startForResult.launch(Intent(requireContext(), SignInActivity::class.java))
        }

        mainActivityViewModel.user.observe(viewLifecycleOwner) {
            //  val id = it.id
            Toast.makeText(requireContext(), it.id.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    lateinit var token: String
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.Main) {
            requireContext().dataStore.data
                .map { preferences ->
                    // No type safety.
                    preferences[TOKEN] ?: ""
                }.collect {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    mainActivityViewModel.getUserInfo(it)
                    token = it
                    binding.tokentv.text = it
                }
        }
        binding.favouriteTrack.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val favouriteArtist =
                    mainActivityViewModel.baseUserService.getFavouriteArtist(token)
                val ftrack = mainActivityViewModel.baseUserService.getFavouriteTracks(token)
               val reArt =  mainActivityViewModel.baseUserService.getRecommendArtists(token)
               val reTr =  mainActivityViewModel.baseUserService.getReommendTracks(token)
                favouriteArtist
                ftrack
            }

        }


    }
}
