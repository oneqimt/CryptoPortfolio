package com.imtmobileapps.cryptoportfolio.view


import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.IOnBackPressed
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import kotlinx.android.synthetic.main.fragment_web.*


class WebFragment : Fragment(), IOnBackPressed {
    
    var selectedUrl: String? = null
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        println("$TAG TEST IN onViewCreated()")
        // if args are not null
        arguments?.let {
            selectedUrl = WebFragmentArgs.fromBundle(it).selectedUrl
        }
        
        // Get the web view settings instance
        val settings = webview.settings
        settings.javaScriptEnabled = true
        settings.setSupportZoom(true)
        settings.loadsImagesAutomatically = true
        //cache - does not seem to work!
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setDomStorageEnabled(true)
        settings.setAppCachePath(activity?.cacheDir?.absolutePath)
        
        webview.fitsSystemWindows = true
        webview.loadUrl(selectedUrl)
        
        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // leave null safety here, in case they hit the back button before finished loading web page
                webprogress?.visibility = View.VISIBLE
            }
            
            override fun onPageFinished(view: WebView, url: String) {
                webprogress?.visibility = View.GONE
            }
        }
        
        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(
                if (webview.isHardwareAccelerated()) View.LAYER_TYPE_HARDWARE else View.LAYER_TYPE_NONE,
                null
            )
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        
        // set the flag
        CoinApp.fromWeb = true
        
    }
    
    
    override fun onBackPressed(): Boolean {
        
        println("$TAG TEST ONBACKPRESSED()")
        if (webview.canGoBack()) {
            // If web view have back history, then go to the web view back history
            webview.goBack()
            return true
        }
        
        return false
        
    }
    
    companion object {
        private val TAG = WebFragment::class.java.simpleName
    }
    
}
