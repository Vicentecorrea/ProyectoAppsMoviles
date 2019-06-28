package com.example.salit.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.salit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaleDetailsActivity : AppCompatActivity() {

    private var currentSaleId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_details)
        getCurrentSaleValues()
    }

    override fun onResume() {
        super.onResume()
        setViewContent()
    }

    private fun setViewContent() {
        GlobalScope.launch(Dispatchers.IO) {
//            val current
        }
    }

    private fun getCurrentSaleValues() {
        currentSaleId = intent.getIntExtra("SALE_ID", 0)
    }
}
