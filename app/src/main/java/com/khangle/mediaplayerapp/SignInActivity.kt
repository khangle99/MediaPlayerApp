package com.khangle.mediaplayerapp

import android.os.Bundle
import android.webkit.*
import android.widget.Toast
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
        binding.signInWV.loadUrl("https://connect.deezer.com/oauth/auth.php?app_id=472082&redirect_uri=https://www.facebook.com/socbaycf/&perms=basic_access,email")
        binding.signInWV.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.let {
                   // Toast.makeText(baseContext, consoleMessage.message(),Toast.LENGTH_SHORT).show()
                }

                return true
            }
        }

        siginInViewModel.accessToken.observe(this, {
            lifecycleScope.launch(Dispatchers.IO) {
                dataStore.edit { settings ->
                    settings[TOKEN] = it
                }
            }
            finish()
//            val data = Intent().also {
//                it.putExtra("token", it)
//            }
//            Log.i("noi dung access token", "onCreate: + ${it} ")
//            Toast.makeText(baseContext, it, Toast.LENGTH_SHORT).show()
//            setResult(Activity.RESULT_OK, data)

        })

    }
    var flag = false
    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
          //  Toast.makeText(baseContext, url, Toast.LENGTH_SHORT).show()

            if (flag) {
                binding.signInWV.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();") {
                    Toast.makeText(baseContext,it,Toast.LENGTH_SHORT).show()
                }
                val b = 9
                return true
            }
            if (url!!.startsWith("https://www.facebook.com/socbaycf/")) {
                val a = url.substringAfter("?code=")
            //    Toast.makeText(baseContext,url.substringAfter("?code="),Toast.LENGTH_SHORT).show()
                siginInViewModel.getToken(url.substringAfter("?code="))
            //    finish()
//                binding.signInWV.loadUrl("https://connect.deezer.com/oauth/access_token.php?app_id=472082&secret=e928543dc74d7f29527e03a45f972a2f&code=${a}")
//                binding.signInWV.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();") {
//                    Log.i("TAG", "${it}")
//                }
                flag = true
            }


            return false
        }


        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
           // Toast.makeText(baseContext, "url", Toast.LENGTH_SHORT).show()
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}





