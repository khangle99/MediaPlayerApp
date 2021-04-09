package com.khangle.mediaplayerapp.di

import android.content.ComponentName
import android.content.Context
import com.khangle.mediaplayerapp.data.network.okhttp.NetworkConnectionInterceptor
import com.khangle.mediaplayerapp.data.network.retrofit.BaseWebservice
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerAuthBaseService
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerAuthService
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerService
import com.khangle.mediaplayerapp.media.MusicService
import com.khangle.mediaplayerapp.media.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideDeezerService(@ApplicationContext context: Context): BaseWebservice {
        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(NetworkConnectionInterceptor(context))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
        return Retrofit.Builder().baseUrl("https://api.deezer.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build().create(DeezerService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeezerAuthService(@ApplicationContext context: Context): DeezerAuthBaseService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(NetworkConnectionInterceptor(context))
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
        return Retrofit.Builder().baseUrl("https://connect.deezer.com/oauth/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient.build())
            .build().create(DeezerAuthService::class.java)
    }


    @Provides
    @Singleton
    fun provideMusicServiceConnection(@ApplicationContext context: Context): MusicServiceConnection {
        return MusicServiceConnection(context, ComponentName(context, MusicService::class.java))
    }

}