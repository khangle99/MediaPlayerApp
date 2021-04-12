package com.khangle.mediaplayerapp.recycleviewadapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.TOKEN
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerBaseUserService
import com.khangle.mediaplayerapp.dataStore
import com.khangle.mediaplayerapp.databinding.FragmentMoreOptionTrackDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoreOptionTrackDialogFragment : BottomSheetDialogFragment() {
    lateinit var track: Track
    lateinit var binding: FragmentMoreOptionTrackDialogBinding
    private val viewmodel: MoreOptionTrackDialogFragmentViewModel by viewModels()
    var isSigned= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = requireArguments().getParcelable("track")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_more_option_track_dialog, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.track = track
        binding.addFavTrack.isVisible = false
        binding.removeFavTrack.isVisible = false
        // kiem tra da login chua
        viewmodel.isFavourite.observe(viewLifecycleOwner, Observer {
            if (it) {
           //     binding.addFavTrack.isVisible = false
                binding.removeFavTrack.isVisible = true
            } else {
                binding.addFavTrack.isVisible = true
           //     binding.removeFavTrack.isVisible = false
            }
        })

        binding.addFavTrack.setOnClickListener {
            if (isSigned && !token.equals("")) {
                viewmodel.addFavouriteTrack(track.id, token)
            }
            dismiss()
        }
        binding.removeFavTrack.setOnClickListener {
            if (isSigned && !token.equals("")) {
            viewmodel.removeFavouriteTrack(track.id, token)
            }
            dismiss()
        }

    }
    var token = ""
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.Main) {
            requireContext().dataStore.data
                .map { preferences ->
                    // No type safety.
                    preferences[TOKEN] ?: ""
                }.collect {
                    // update UI when listen login status
                    if (!it.equals("")) {
                        viewmodel.isInFavouriteTrack(track.id,it)
                        isSigned = true
                        token = it
                    } else {
                        isSigned = false
                        token = ""
                    }
                }
        }

    }

}
@HiltViewModel
class MoreOptionTrackDialogFragmentViewModel @Inject constructor(private val baseUserService: DeezerBaseUserService): ViewModel() {
    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    fun isInFavouriteTrack(trackId: Long, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favouriteTracks = baseUserService.getFavouriteTracks(token)
            _isFavourite.postValue(favouriteTracks.data.findLast { it.id ==  trackId} != null)
        }
    }

    fun addFavouriteTrack(trackId: Long,token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            baseUserService.addFavouriteTracks(token,trackId)
        }
    }

    fun removeFavouriteTrack(trackId: Long,token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            baseUserService.removeFavouriteTracks(token,trackId)
        }
    }
}