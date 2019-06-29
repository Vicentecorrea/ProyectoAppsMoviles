package com.example.salit.fragments


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.salit.Constants

import com.example.salit.R
import com.example.salit.activities.SaleDetailsActivity
import com.example.salit.activities.WebViewActivity
import com.example.salit.adapter.SalesAdapter
import com.example.salit.db.AppDatabase
import com.example.salit.db.dao.CategoryDao
import com.example.salit.db.models.Sale
import kotlinx.android.synthetic.main.fragment_create_sale.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search_sale.*
import kotlinx.android.synthetic.main.fragment_search_sale.salesListView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SearchSaleFragment : Fragment() {

    private var currentCategory = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinnerCategories()
        setListeners()
        setListOnClickListener()
    }

    private fun setSpinnerCategories() {
        val database = AppDatabase.getDatabase(context!!)
        val categoryDao = database.CategoryDao()
        GlobalScope.launch(Dispatchers.IO){
            val categoriesObjects = categoryDao.getAll()
            launch(Dispatchers.Main){
                val categories = mutableListOf<String>()
                for (category in categoriesObjects){
                    categories.add(category.name!!)
                }
                val spinnerArray =
                    ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, categories)
                spinnerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = spinnerArray
            }
        }

    }

    private fun setListeners() {
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val categoryName = categorySpinner.selectedItem.toString()
                GlobalScope.launch(Dispatchers.IO) {
                    val categoryDao = AppDatabase.getDatabase(context!!).CategoryDao()
                    currentCategory = categoryDao.getCategoryId(categoryName)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        buttonSearch.setOnClickListener {
            changeListView()
        }
    }

    private fun changeListView() {
        val database = AppDatabase.getDatabase(context!!)
        val saleDao = database.SaleDao()
        GlobalScope.launch(Dispatchers.IO) {
            if (searchEditText.text.toString().isBlank() && currentCategory != 1) {
                val sales = saleDao.getSalesByCategory(currentCategory)
                launch(Dispatchers.Main) {
                    val itemsAdapter = SalesAdapter(context!!, ArrayList(sales))
                    salesListView.adapter = itemsAdapter
                }
            } else if (searchEditText.text.toString().isNotBlank() && currentCategory == 1) {
                val sales = saleDao.getSalesByName("%" + searchEditText.text.toString() + "%")
                launch(Dispatchers.Main) {
                    val itemsAdapter = SalesAdapter(context!!, ArrayList(sales))
                    salesListView.adapter = itemsAdapter
                }
            } else if (searchEditText.text.toString().isNotBlank() && currentCategory != 1) {
                val sales =
                    saleDao.getSalesByNameAndCategory("%" + searchEditText.text.toString() + "%", currentCategory)
                launch(Dispatchers.Main) {
                    val itemsAdapter = SalesAdapter(context!!, ArrayList(sales))
                    salesListView.adapter = itemsAdapter
                }
            } else if (searchEditText.text.toString().isBlank() && currentCategory == 1) {
                val sales = saleDao.getAll()
                launch(Dispatchers.Main) {
                    val itemsAdapter = SalesAdapter(context!!, ArrayList(sales))
                    salesListView.adapter = itemsAdapter
                }
            }
        }
    }

    private fun setListOnClickListener() {
        salesListView.setOnItemClickListener { _, _, position, _ ->
            val selectedSale = (salesListView.adapter).getItem(position) as Sale
            if (selectedSale.isOnline) {
                startActivity(
                    Intent(context, WebViewActivity::class.java).
                        putExtra("SALE_LINK", selectedSale.link))
            } else {
                startActivity(
                    Intent(context, SaleDetailsActivity::class.java).
                        putExtra("SALE_ID", selectedSale.id)
                )
            }
        }
    }


}
