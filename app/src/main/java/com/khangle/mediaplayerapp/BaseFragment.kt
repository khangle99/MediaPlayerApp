package com.khangle.mediaplayerapp


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.khangle.mediaplayerapp.util.NetworkUtil


abstract class BaseFragment : Fragment() {
    var isFirstLoad = true
    val networkReceiver = NetworkChangeReceiver()
    val filter = IntentFilter().also {
        it.addAction("android.net.conn.CONNECTIVITY_CHANGE")
    }
    abstract fun refresh()
    override fun onResume() {
        super.onResume()
       requireContext().registerReceiver(networkReceiver, filter)
        if (view == null) {
            return
        }
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                parentFragmentManager.popBackStackImmediate()
                true
            } else false
        }
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(networkReceiver)
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {


        override fun onReceive(context: Context, intent: Intent) {
            val status = NetworkUtil.getConnectivityStatusString(context)
            if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
                if (!isFirstLoad && (status == NetworkUtil.NETWORK_STATUS_WIFI ||status == NetworkUtil.NETWORK_STATUS_MOBILE)) {
                    refresh()
                } else {
                    isFirstLoad = false
                }
            }
        }


    }

}