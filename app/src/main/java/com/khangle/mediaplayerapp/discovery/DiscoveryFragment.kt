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
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.khangle.mediaplayerapp.BaseFragment
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentDiscoveryBinding
import com.khangle.mediaplayerapp.discovery.fragments.catalog.CatalogFragment
import com.khangle.mediaplayerapp.discovery.fragments.searchResult.SearchResultFragment


class DiscoveryFragment : BaseFragment() {

    private lateinit var discoveryViewModel: DiscoveryViewmodel
    private lateinit var binding: FragmentDiscoveryBinding
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

    override fun refresh() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.commit {
            replace(R.id.nav_discovery_fragment, CatalogFragment(), "catalog")
            addToBackStack("")
        }
        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val bundle = bundleOf("query" to query)
                childFragmentManager.commit {
                    replace(R.id.nav_discovery_fragment, SearchResultFragment().also { it.arguments = bundle }, "result")
                    addToBackStack("searchStart")
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