package com.khangle.mediaplayerapp.chartDetail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.khangle.mediaplayerapp.MainActivityViewModel
import com.khangle.mediaplayerapp.R
import com.khangle.mediaplayerapp.data.model.Genre
import com.khangle.mediaplayerapp.databinding.FragmentChartDetailBinding
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
    lateinit var adapter: ChartDetailAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val genre: Genre = it.getParcelable("genre")!!
            viewmodel.loadChartTracks(genre.id)
            adapter = ChartDetailAdapter(genre, {
                mainActivityViewModel.play(
                    it.id.toString(),
                    adapter.currentList
                )
            })

        }
        binding.chartTrack.adapter = adapter
        binding.chartTrack.layoutManager = LinearLayoutManager(requireContext())
        viewmodel.chartTracks.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

}


open class BaseFullscreenDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
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
}