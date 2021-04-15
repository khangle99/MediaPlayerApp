package com.khangle.mediaplayerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khangle.mediaplayerapp.databinding.FragmentLyricBottomSheetBinding
import com.khangle.mediaplayerapp.home.fragments.chartDetail.BaseFullscreenDialogFragment


class LyricBottomSheetFragment(val lyric: String) : BaseFullscreenDialogFragment() {
    lateinit var binding: FragmentLyricBottomSheetBinding
    override fun refresh() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_lyric_bottom_sheet,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lyricTextview.text = lyric
    }

}