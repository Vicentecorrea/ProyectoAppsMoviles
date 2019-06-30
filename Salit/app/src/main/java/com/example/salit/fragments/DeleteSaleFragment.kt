package com.example.salit.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.salit.CredentialsManager

import com.example.salit.R
import com.example.salit.activities.MainActivity
import com.example.salit.activities.SaleDetailsActivity
import com.example.salit.activities.WebViewActivity
import com.example.salit.adapter.SalesAdapter
import com.example.salit.db.AppDatabase
import com.example.salit.db.models.Sale
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class DeleteSaleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_sale, container, false)
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
            val currentUserEmail = CredentialsManager.getInstance(context!!).loadUser()!!.first
            val sales = saleDao.getUserSales(currentUserEmail)
            launch(Dispatchers.Main){
                val itemsAdapter = SalesAdapter(context!!, ArrayList(sales))
                salesListView.adapter = itemsAdapter
            }
        }
    }

    private fun setListOnClickListener() {
        salesListView.setOnItemClickListener { _, _, position, _ ->
            val selectedSale = (salesListView.adapter).getItem(position) as Sale
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Delete sale")
            builder.setMessage("Are you sure that you want to delete the sale '" + selectedSale.name + "'?")
            builder.setPositiveButton("Yes") { dialog, which ->
                val database = AppDatabase.getDatabase(context!!)
                val saleDao = database.SaleDao()
                GlobalScope.launch(Dispatchers.IO) {
                    saleDao.deleteSale(selectedSale.id)
                }
                Toast.makeText(context!!, "The sale was successfully deleted", Toast.LENGTH_SHORT).show()
                loadSales()
            }
            builder.setNegativeButton("No") { dialog, which ->
            }
            builder.show()
        }
    }
}
