package com.khangle.mediaplayerapp.di

import com.khangle.mediaplayerapp.data.repo.DeezerRepository
import com.khangle.mediaplayerapp.data.repo.DeezerRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface MainVMModule {
    @Binds
    @ViewModelScoped
    abstract fun bindChartRepo(deezerRepositoryImp: DeezerRepositoryImp): DeezerRepository
}