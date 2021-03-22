package com.khangle.mediaplayerapp

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MediaPlayerApp: Application() {

    private  val TAG = "MediaPlayerApp"
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
//        val connectivityManager = getSystemService(ConnectivityManager::class.java)
//        val request = NetworkRequest.Builder()
//            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
//            .addCapability(NET_CAPABILITY_INTERNET)
//            .addCapability(NET_CAPABILITY_VALIDATED)
//            .build()
//        connectivityManager.registerNetworkCallback(request,object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network : Network) {
//                Log.e(TAG, "The default network is now: " + network)
//                networkCallback?.invoke(true)
//            }
//
//            override fun onLost(network : Network) {
//                Log.e(TAG, "The application no longer has a default network. The last default network was " + network)
//                networkCallback?.invoke(false)
//            }
//
//            override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
//                Log.e(TAG, "The default network changed capabilities: " + networkCapabilities)
//            }
//
//            override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {
//                Log.e(TAG, "The default network changed link properties: " + linkProperties)
//            }
//        })
    }



    companion object {
        var networkCallback: ((Boolean) -> Unit)? = null
    }
}