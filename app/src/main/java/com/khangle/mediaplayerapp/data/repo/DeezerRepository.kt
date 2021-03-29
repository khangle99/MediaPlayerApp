package com.khangle.mediaplayerapp.data.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.khangle.mediaplayerapp.data.model.*
import com.khangle.mediaplayerapp.data.network.retrofit.BaseWebservice
import com.khangle.mediaplayerapp.exception.NoResultException
import javax.inject.Inject

//return lai model, khong can lien quan network, thao tac du lieu voi track
interface DeezerRepository {
    suspend fun getChartsTracks(): List<Track> // do chi co 10 item cung nen k can phan trang ,
    suspend fun getGenres(): List<Genre>
    fun querryTrack(querryText: String): TrackPagingSource // doi kieu return ve pager de phan trang
    fun querryArtist(querryText: String): ArtistPagingSource // doi kieu return ve pager de phan trang
    suspend fun getCompatArtistTrackList(artistId: String): List<Track> // do chi co 15 item cung nen k can phan trang ,
    // se co ham top track phan trang, show o 1 fragment rieng
    suspend fun getRelateArtist(artistId: String): List<Artist> // lay cung 20 item artist
    fun querryAlbum(querryText: String): PlaylistPagingSource // doi kieu return ve pager de phan trang
    suspend fun getCompatPlaylistTracks(playlistId: String): List<Track>
}

private const val TAG = "EditorialRepository"

class DeezerRepositoryImp @Inject constructor(private val baseWebservice: BaseWebservice) :
    DeezerRepository {

    override suspend fun getChartsTracks(): List<Track> {
        return baseWebservice.getCharts().tracks.data
    }

    override suspend fun getGenres(): List<Genre> {
        return baseWebservice.getGenres().data
    }

    override fun querryTrack(querryText: String): TrackPagingSource {
        return TrackPagingSource(baseWebservice, querryText)
    }

    override fun querryArtist(querryText: String): ArtistPagingSource {
        return ArtistPagingSource(baseWebservice, querryText)
    }

    override suspend fun getCompatArtistTrackList(artistId: String): List<Track> {
        return baseWebservice.topTrackOfArtist(artistId,0).data
    }

    override suspend fun getRelateArtist(artistId: String): List<Artist> {
        return baseWebservice.relateArtist(artistId).data
    }

    override fun querryAlbum(querryText: String): PlaylistPagingSource {
        return PlaylistPagingSource(baseWebservice,querryText)
    }

    override suspend fun getCompatPlaylistTracks(playlistId: String): List<Track> {
        return baseWebservice.playlistTrack(playlistId, 0).data
    }


}
class ArtistPagingSource(
    private val baseWebservice: BaseWebservice,
    val query: String,
): PagingSource<Int, Artist>() {
    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(25) ?: anchorPage?.nextKey?.minus(25)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        try {
            val nextPageNumber = params.key ?: 0
            val response: ArtistListRespone = baseWebservice.searchArtist(query, nextPageNumber)
            if (response.data.size == 0 && response.total == 0) {
                return LoadResult.Error(NoResultException())
            }
            var next: Int? = null
            var remain = 0
            if (response.next != null) {
                val nextString = response.next
                val start = nextString.lastIndexOf("=") + 1
                val end = nextString.length
                next = response.next.substring(start, end).toInt()
                remain = response.total - next
            }

            return LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = next,
                itemsAfter = remain
            )
        } catch (e: Exception) {

            return LoadResult.Error(e)
        }
    }


}

class PlaylistPagingSource(
    private val baseWebservice: BaseWebservice,
    val query: String,
): PagingSource<Int, Playlist>() {
    override fun getRefreshKey(state: PagingState<Int, Playlist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(25) ?: anchorPage?.nextKey?.minus(25)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Playlist> {
        try {
            val nextPageNumber = params.key ?: 0
            val response: PlaylistListRespone = baseWebservice.searchPlaylist(query, nextPageNumber)
            if (response.data.size == 0 && response.total == 0) {
                return LoadResult.Error(NoResultException())
            }
            var next: Int? = null
            var remain = 0
            if (response.next != null) {
                val nextString = response.next
                val start = nextString.lastIndexOf("=") + 1
                val end = nextString.length
                next = response.next.substring(start, end).toInt()
                remain = response.total - next
            }

            return LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = next,
                itemsAfter = remain
            )
        } catch (e: Exception) {

            return LoadResult.Error(e)
        }
    }


}

class TrackPagingSource(
    private val baseWebservice: BaseWebservice,
    val query: String
) : PagingSource<Int, Track>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Track> {
        try {
            val nextPageNumber = params.key ?: 0
            val response = baseWebservice.searchTrack(query, nextPageNumber)
            if (response.data.size == 0 && response.total == 0) {
                return LoadResult.Error(NoResultException())
            }
            var next: Int? = null
            var remain = 0
            if (response.next != null) {
                val nextString = response.next
                val start = nextString.lastIndexOf("=") + 1
                val end = nextString.length
                next = response.next.substring(start, end).toInt()
                remain = response.total - next
            }

            return LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = next,
                itemsAfter = remain
            )
        } catch (e: Exception) {

            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Track>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.


        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(25) ?: anchorPage?.nextKey?.minus(25)
        }
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
