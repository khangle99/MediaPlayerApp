package com.khangle.mediaplayerapp.discovery.fragments.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.databinding.FragmentCatalogBinding
import com.khangle.mediaplayerapp.recycleviewadapter.GenreAdapter
import com.khangle.mediaplayerapp.recycleviewadapter.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatalogFragment : Fragment() {


    private val catalogViewModel: CatalogViewModel by viewModels()
    lateinit var binding: FragmentCatalogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalog,container,false)
        binding.lifecycleOwner= viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.genreRecyclerview.layoutManager = GridLayoutManager(context, 2)
        val adapter = GenreAdapter { genre ->

        }
        binding.genreRecyclerview.adapter = adapter
        catalogViewModel.genreList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.genreRecyclerview.addItemDecoration(SpacesItemDecoration(50))
    }


}