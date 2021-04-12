package com.khangle.mediaplayerapp.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.TOKEN
import com.khangle.mediaplayerapp.dataStore
import com.khangle.mediaplayerapp.databinding.FragmentLibraryBinding
import com.khangle.mediaplayerapp.library.fragments.mainLibrary.MainLibraryFragment
import com.khangle.mediaplayerapp.library.fragments.signIn.SignInFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LibraryFragment : Fragment() {

    public val libraryViewModel: LibraryViewModel by viewModels()
    private lateinit var binding: FragmentLibraryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_library, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

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
                        // chuyen fragment main
                        childFragmentManager.commit {
                            setReorderingAllowed(true)
                            val mainLibraryFragment = MainLibraryFragment()
                            mainLibraryFragment.arguments = bundleOf("token" to it)
                            replace(R.id.libraryHost, mainLibraryFragment)
                        }
                        libraryViewModel.getUserInfo(it)
                    } else {
                        // chuyen fragment login
                        childFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(R.id.libraryHost, SignInFragment())
                        }
                    }
                }
        }
    }
}
