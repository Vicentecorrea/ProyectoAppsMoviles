package com.example.salit.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.salit.R
import com.example.salit.activities.SaleDetailsActivity
import com.example.salit.activities.WebViewActivity
import com.example.salit.adapter.SalesAdapter
import com.example.salit.db.AppDatabase
import com.example.salit.db.models.Sale
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList







class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSales()
        setListOnClickListener()
    }

    private fun loadSales(){
        val database = AppDatabase.getDatabase(context!!)
        val saleDao = database.SaleDao()
        GlobalScope.launch(Dispatchers.IO){
            val sales = saleDao.getAll()
            val today = Calendar.getInstance().time
            val salesToDelete = arrayListOf<Sale>()
            for (sale in sales){
                val cal = Calendar.getInstance()
                val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                cal.time = sdf.parse(sale.createdAt) // all done
                cal.add(Calendar.MONTH, 1)
                if (today > cal.time){
                        salesToDelete.add(sale)
                }
            }
            for (sale in salesToDelete){
                saleDao.deleteSale(sale.id)
            }
            val finalSales = saleDao.getAll()

//            val filteredSales = sales.filter { Calendar.getInstance().time = it.createdAt > today }
            launch(Dispatchers.Main){
                val itemsAdapter = SalesAdapter(context!!, ArrayList(finalSales))
                salesListView.adapter = itemsAdapter
            }
        }
    }

    private fun setListOnClickListener() {
        salesListView.setOnItemClickListener { _, _, position, _ ->
            val selectedSale = (salesListView.adapter).getItem(position) as Sale
            if (selectedSale.isOnline) {
                startActivity(
                    Intent(context, WebViewActivity::class.java).putExtra("SALE_LINK", selectedSale.link)
                )
            } else {
                startActivity(
                    Intent(context, SaleDetailsActivity::class.java).putExtra("SALE_ID", selectedSale.id)
                )
            }
        }
    }
}
