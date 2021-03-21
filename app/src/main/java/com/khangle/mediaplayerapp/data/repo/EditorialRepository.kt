package com.khangle.mediaplayerapp.data.repo

import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.network.retrofit.BaseWebservice
import javax.inject.Inject
//return lai model, khong can lien quan network, thao tac du lieu voi track
interface EditorialRepository {

    suspend fun getChartsTracks() : List<Track> // do chi co 10 item cung nen k can phan trang ,
}

class EditorialRepositoryImp @Inject constructor(private val baseWebservice: BaseWebservice) : EditorialRepository {

    override suspend fun getChartsTracks(): List<Track> {
        return baseWebservice.getCharts().tracks.data
    }

}


//class TrackCharSource @Inject constructor(val baseWebservice: BaseWebservice) : PagingSource<Int, Track>() {
//    override fun getRefreshKey(state: PagingState<Int, Track>): Int? {
//
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
//        TODO("Not yet implemented")
//    }
//
//}
