package com.khangle.mediaplayerapp.home.fragments.chartDetail

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Genre
import com.khangle.mediaplayerapp.data.model.Resource
import com.khangle.mediaplayerapp.databinding.FragmentChartDetailBinding
import com.khangle.mediaplayerapp.util.NetworkUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartDetailDialogFragment : BaseFullscreenDialogFragment() {

    lateinit var binding: FragmentChartDetailBinding
    private val viewmodel: ChartDetailDialogViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chart_detail,
            container,
            false
        )
        return binding.root
    }

    override fun refresh() {
        viewmodel.loadChartTracks(genre.id)
    }
    lateinit var  genre: Genre
    lateinit var adapter: ChartDetailAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            genre = it.getParcelable("genre")!!
            viewmodel.loadChartTracks(genre.id)
            adapter = ChartDetailAdapter(parentFragmentManager,genre) {
                mainActivityViewModel.play(
                    it,
                    adapter.currentList
                )
            }

        }
        binding.chartTrack.adapter = adapter
        binding.chartTrack.layoutManager = LinearLayoutManager(requireContext())
        viewmodel.chartTracks.observe(viewLifecycleOwner, {
            when (it)
            {
                is Resource.Success -> { adapter.submitList(it.data) ; binding.progressBar.visibility = View.INVISIBLE }
                is Resource.Loading -> { binding.progressBar.visibility = View.VISIBLE }
                is Resource.Error -> {Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show(); binding.progressBar.visibility = View.INVISIBLE }
            }

        })
    }

}


abstract class BaseFullscreenDialogFragment : BottomSheetDialogFragment() {
    abstract fun refresh()
    var isFirstLoad = true
    val networkReceiver = NetworkChangeReceiver()
    val filter = IntentFilter().also {
        it.addAction("android.net.conn.CONNECTIVITY_CHANGE")
    }
    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(networkReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(networkReceiver)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                val behaviour = BottomSheetBehavior.from(view)
                setupFullHeight(view)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = NetworkUtil.getConnectivityStatusString(context)
            if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
                if (!isFirstLoad && (status == NetworkUtil.NETWORK_STATUS_WIFI ||status == NetworkUtil.NETWORK_STATUS_MOBILE)) {
                    refresh()
                } else {
                    isFirstLoad = false
                }
            }
        }


    }
}