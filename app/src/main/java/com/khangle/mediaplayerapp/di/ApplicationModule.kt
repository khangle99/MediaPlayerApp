package com.khangle.mediaplayerapp.di

import android.content.ComponentName
import android.content.Context
import com.khangle.mediaplayerapp.data.network.retrofit.BaseWebservice
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerService
import com.khangle.mediaplayerapp.media.MusicService
import com.khangle.mediaplayerapp.media.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideDeezerService(): BaseWebservice {
        return Retrofit.Builder().
        baseUrl("https://api.deezer.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(DeezerService::class.java)
    }



    @Provides
    @Singleton
    fun provideMusicServiceConnection(@ApplicationContext context: Context): MusicServiceConnection {
        return MusicServiceConnection(context, ComponentName(context, MusicService::class.java))
    }

}