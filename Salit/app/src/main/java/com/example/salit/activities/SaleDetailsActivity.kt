package com.example.salit.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.salit.CredentialsManager
import com.example.salit.R
import com.example.salit.db.AppDatabase
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
            val currentUserEmail = CredentialsManager.getInstance(baseContext).loadUser()!!.first
            val appDatabase = AppDatabase.getDatabase(baseContext)
            val saleDao = appDatabase.SaleDao()
            val selectedSale = saleDao.getSaleById(currentSaleId)
//            launch(Dispatchers.Main) {
//
//            }
        }
    }

    private fun getCurrentSaleValues() {
        currentSaleId = intent.getIntExtra("SALE_ID", 0)
    }
}
