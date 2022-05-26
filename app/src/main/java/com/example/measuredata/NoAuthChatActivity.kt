package com.example.measuredata

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.webkit.WebView
import android.webkit.WebViewClient


class NoAuthChatActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_auth_chat)
        val webView=findViewById<WebView>(R.id.web_view)
        webView.settings.setJavaScriptEnabled(true)
        val sharedPreferences = getSharedPreferences("savedSettings", Context.MODE_PRIVATE)
        var url = sharedPreferences.getString("email", null).toString()
        var patient_id = sharedPreferences.getString("doctorID", null).toString()
        url=url+"/no-auth/chat/"+patient_id
        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        val curl=url
        if (curl != null) {
            Toast.makeText(this, curl, Toast.LENGTH_SHORT).show()
            webView.loadUrl(curl)

        }else{
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
        }

    }
}