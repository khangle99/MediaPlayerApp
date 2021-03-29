package com.khangle.mediaplayerapp.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentDiscoveryBinding


class DiscoveryFragment : Fragment() {
    private lateinit var discoveryViewModel: DiscoveryViewmodel
    private lateinit var binding: FragmentDiscoveryBinding
    private lateinit var navController: NavController
    private val TAG = "DiscoveryFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        discoveryViewModel =
            ViewModelProvider(this).get(DiscoveryViewmodel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discovery, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_discovery_fragment) as NavHostFragment?
        navController = navHostFragment?.navController!!
        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val bundle = bundleOf("querry" to query)
                if (navController.currentDestination!!.id == R.id.searchResultFragment) {
                    navController.navigate(R.id.action_searchResultFragment_self, bundle)
                } else {
                    navController.navigate(
                        R.id.action_catalogFragment_to_searchResultFragment,
                        bundle
                    )
                }
                hideSoftKeyboard(this@DiscoveryFragment.requireView())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    fun hideSoftKeyboard(view: View) {
        if (view.requestFocus()) {

            val imm = getSystemService(
                requireContext(),
                InputMethodManager::class.java
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }
}