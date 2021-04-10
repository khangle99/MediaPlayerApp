package com.khangle.mediaplayerapp.di

import android.content.ComponentName
import android.content.Context
import com.khangle.mediaplayerapp.data.network.okhttp.NetworkConnectionInterceptor
import com.khangle.mediaplayerapp.data.network.retrofit.*
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
    fun provideDeezerService(okHttpClient: OkHttpClient): BaseWebservice {
        return Retrofit.Builder().baseUrl("https://api.deezer.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(DeezerService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeezerAuthService(okHttpClient: OkHttpClient): DeezerAuthBaseService {
        return Retrofit.Builder().baseUrl("https://connect.deezer.com/oauth/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build().create(DeezerAuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeezerUserService(okHttpClient: OkHttpClient): DeezerBaseUserService {
        return Retrofit.Builder().baseUrl("https://api.deezer.com/user/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(DeezerUserService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkhttpCLient(@ApplicationContext context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return  OkHttpClient.Builder()
                .addInterceptor(NetworkConnectionInterceptor(context))
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).build()

    }



    @Provides
    @Singleton
    fun provideMusicServiceConnection(@ApplicationContext context: Context): MusicServiceConnection {
        return MusicServiceConnection(context, ComponentName(context, MusicService::class.java))
    }

}