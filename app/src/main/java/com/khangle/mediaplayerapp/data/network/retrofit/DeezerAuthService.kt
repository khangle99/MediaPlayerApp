package com.khangle.mediaplayerapp.data.network.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerAuthBaseService {
     fun fetchAccessToken(appId: String, secret: String, code: String): Call<String>
}
interface DeezerAuthService: DeezerAuthBaseService {
    @GET("access_token.php")
    override  fun fetchAccessToken(@Query("app_id") appId: String, @Query("secret") secret: String, @Query("code") code: String): Call<String>
}
