package com.khangle.mediaplayerapp.data.network.retrofit

import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.ChartSelectionReponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BaseWebservice {
    suspend fun getCharts(): ChartSelectionReponse
    suspend fun getTrack(trackId: String) : Track
}

interface DeezerService: BaseWebservice {



    @GET("/editorial/0/charts")
    override suspend fun getCharts(): ChartSelectionReponse // khong co tham so phan trang

    @GET("/track/{id}")
    override suspend fun getTrack(@Path("id")  trackId: String): Track
}