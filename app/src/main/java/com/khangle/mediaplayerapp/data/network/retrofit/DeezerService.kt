package com.khangle.mediaplayerapp.data.network.retrofit

import com.khangle.mediaplayerapp.data.network.ChartSelectionReponse
import retrofit2.http.GET

interface BaseDao {
    suspend fun getCharts(): ChartSelectionReponse
}

interface DeezerService: BaseDao {
    @GET("/editorial/0/charts")
    override suspend fun getCharts(): ChartSelectionReponse
}