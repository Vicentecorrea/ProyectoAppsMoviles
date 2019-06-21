package com.example.salit

import android.content.Context
import android.webkit.JavascriptInterface

class WebAppInterface(private val mContext: Context){

    @JavascriptInterface
    fun finishActivity(){
        System.exit(0)
    }
}