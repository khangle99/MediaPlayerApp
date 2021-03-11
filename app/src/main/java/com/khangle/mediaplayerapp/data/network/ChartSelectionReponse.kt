package com.khangle.mediaplayerapp.data.network

import com.khangle.mediaplayerapp.data.model.Track

data class ChartSelectionReponse ( // editorial/0/charts
    val tracks: TrackListRespone
)
data class TrackListRespone(
    val total: Int,
    val data: List<Track>
)