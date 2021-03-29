package com.khangle.mediaplayerapp.discovery.fragments.searchResult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.khangle.mediaplayerapp.data.model.Artist
import com.khangle.mediaplayerapp.data.model.Playlist
import com.khangle.mediaplayerapp.data.model.Track
import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel  @Inject constructor(val repository: DeezerRepository): ViewModel() {
    val searchQuerry = MutableLiveData<String>().apply { value = "" }
     fun querryTrack(querryText: String): Flow<PagingData<Track>> {
            val querryTrack = repository.querryTrack(querryText)
            return Pager(
                // Configure how data is loaded by passing additional properties to
                // PagingConfig, such as prefetchDistance.
                PagingConfig(25,1,true,1)

            ) {
                querryTrack
            }.flow
                .cachedIn(viewModelScope)

    }

    fun querryArtist(querryText: String): Flow<PagingData<Artist>> {
        val querryTrack = repository.querryArtist(querryText)
        return Pager(
            PagingConfig(25,1,true,1)
        ) {
            querryTrack
        }.flow
            .cachedIn(viewModelScope)

    }

    fun querryPlaylist(querryText: String): Flow<PagingData<Playlist>> {
        val querryTrack = repository.querryAlbum(querryText)
        return Pager(
            PagingConfig(25,1,true,1)
        ) {
            querryTrack
        }.flow
            .cachedIn(viewModelScope)

    }
}