package com.khangle.mediaplayerapp.discovery.fragments.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.khangle.mediaplayerapp.BaseFragment
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.databinding.FragmentCatalogBinding
import com.khangle.mediaplayerapp.recycleviewadapter.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatalogFragment : BaseFragment() {

    private val catalogViewModel: CatalogViewModel by viewModels()
    lateinit var binding: FragmentCatalogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("da", "onCreate: tao lai")
        catalogViewModel.refresh()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("dasd", "onCreateView: ")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalog,container,false)
        binding.lifecycleOwner= viewLifecycleOwner

        binding.genreRecyclerview.layoutManager = GridLayoutManager(context, 2)
        val adapter = GenreAdapter { genre ->

        }
        binding.genreRecyclerview.adapter = adapter
        catalogViewModel.genreList.observe(viewLifecycleOwner) {
            when (it)
            {
                is Resource.Success -> { adapter.submitList(it.data) ; binding.catalogProgress.visibility = View.INVISIBLE }
                is Resource.Loading -> { binding.catalogProgress.visibility = View.VISIBLE }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show(); binding.catalogProgress.visibility = View.INVISIBLE }
            }
        }
        binding.genreRecyclerview.addItemDecoration(SpacesItemDecoration(50))



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun refresh() {
        catalogViewModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("dasda", "onDestroy: ")
    }
}