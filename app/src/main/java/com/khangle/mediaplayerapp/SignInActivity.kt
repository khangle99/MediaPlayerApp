package com.khangle.mediaplayerapp

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.khangle.mediaplayerapp.databinding.ActivitySignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val TOKEN = stringPreferencesKey("token")

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var myWebView: WebView
    val siginInViewModel: MainActivityViewModel by viewModels()
    val USER_AGENT =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:61.0) Gecko/20100101 Firefox/61.0"
    val authUrl = "https://connect.deezer.com/oauth/auth.php?app_id=472082&redirect_uri=https://www.facebook.com/socbaycf/&perms=basic_access,email,manage_library,delete_library,offline_access&response_type=token"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signInWV.webViewClient = MyWebViewClient()
        binding.signInWV.clearHistory()
        binding.signInWV.clearCache(true)
        binding.signInWV.settings.javaScriptEnabled = true
        binding.signInWV.settings.allowContentAccess = true
        binding.signInWV.settings.setJavaScriptCanOpenWindowsAutomatically(true);
        binding.signInWV.settings.setSupportMultipleWindows(true)
        binding.signInWV.settings.userAgentString = USER_AGENT
        binding.signInWV.loadUrl(authUrl)

        siginInViewModel.accessToken.observe(this, {
            lifecycleScope.launch(Dispatchers.IO) {
                dataStore.edit { settings ->
                    settings[TOKEN] = it
                }
            }
            finish()
        })
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url!!.startsWith("https://www.facebook.com/socbaycf/")) {
                val a = url.substringAfter("access_token=")
                val token = a.substringBeforeLast("&expires")
                siginInViewModel.setToken(token)
            }

            return false
        }
    }
}





