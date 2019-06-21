package com.example.salit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.salit.R
import com.example.salit.WebAppInterface
import kotlinx.android.synthetic.main.content_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        setWebView()
    }

    private fun setWebView() {
        saleWebView.settings.javaScriptEnabled = true
        saleWebView.addJavascriptInterface(WebAppInterface(this), "Android")
        var link = intent.getStringExtra("SALE_LINK")
        Toast.makeText(this, link, Toast.LENGTH_SHORT).show()
        saleWebView.loadUrl(link)
    }
}
