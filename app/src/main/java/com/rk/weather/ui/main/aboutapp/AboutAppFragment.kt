package com.rk.weather.ui.main.aboutapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rk.weather.R


class AboutAppFragment : Fragment() {

    var webView: WebView? = null
    var fabClose: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        webView = view?.findViewById(R.id.webview)
        fabClose = view?.findViewById(R.id.fabClose)
        val webSettings = webView!!.settings
        webSettings.javaScriptEnabled = true

        val webViewClient = WebViewClientImpl(requireActivity())
        webView!!.webViewClient = webViewClient

        webView!!.loadUrl("https://github.com/rajnikanth8008/Weather/blob/master/README.md")
        fabClose?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}