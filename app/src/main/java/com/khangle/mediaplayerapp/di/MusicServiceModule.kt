package com.khangle.mediaplayerapp.di

import com.khangle.mediaplayerapp.data.repo.MusicServiceRepository
import com.khangle.mediaplayerapp.data.repo.MusicServiceRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
interface MusicServiceModule {
    @Binds
    @ServiceScoped
    abstract fun bindChartRepo(musicServiceRepositoryImp: MusicServiceRepositoryImp): MusicServiceRepository
}