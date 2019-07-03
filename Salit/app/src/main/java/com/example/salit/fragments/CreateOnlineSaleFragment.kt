package com.example.salit.fragments

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.salit.CredentialsManager
import com.example.salit.Constants
import com.example.salit.activities.MainActivity
import com.example.salit.db.AppDatabase
import com.example.salit.db.models.Sale
import kotlinx.android.synthetic.main.fragment_create_online_sale.*
import kotlinx.android.synthetic.main.fragment_create_sale.*
import kotlinx.android.synthetic.main.fragment_create_online_sale.createSaleButton
import kotlinx.android.synthetic.main.fragment_create_online_sale.normalPriceInput
import kotlinx.android.synthetic.main.fragment_create_online_sale.offerPriceInput
//import kotlinx.android.synthetic.main.fragment_create_online_sale.saleDescriptionEditText
import kotlinx.android.synthetic.main.fragment_create_online_sale.saleNameEditText
import kotlinx.android.synthetic.main.fragment_create_online_sale.spinnerCategories
import kotlinx.android.synthetic.main.fragment_create_online_sale.linkEditText
import kotlinx.android.synthetic.main.list_item_sale.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class CreateOnlineSaleFragment : Fragment() {

    private var category = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.salit.R.layout.fragment_create_online_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinnerCategories()
        addSpinnerCategoryListener()
        createSaleButton.setOnClickListener {
            createOnlineSale()
        }
    }

    private fun addSpinnerCategoryListener() {
        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val categoryName = spinnerCategories.selectedItem.toString()
                GlobalScope.launch(Dispatchers.IO) {
                    val categoryDao = AppDatabase.getDatabase(context!!).CategoryDao()
                    category = categoryDao.getCategoryId(categoryName)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setSpinnerCategories() {
        val categories = mutableListOf<String>()
        GlobalScope.launch(Dispatchers.IO) {
            val categoryDao = AppDatabase.getDatabase(context!!).CategoryDao()
            val categoryObjects = categoryDao.getAll()
            launch(Dispatchers.Main) {
                for (category in categoryObjects){
                    categories.add(category.name!!)
                }
                val spinnerArray = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, categories)

                spinnerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategories!!.adapter = spinnerArray
            }
        }

    }


    private fun createOnlineSale() {
        val saleObject = createOnlineSaleObject()
        if (saleObject.name != "") {
            storeOnlineSaleObject(saleObject)
        }
    }

    private fun storeOnlineSaleObject(saleObject: Sale) {
        val saleDao = AppDatabase.getDatabase(context!!).SaleDao()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                saleDao.insertAll(saleObject)

                launch(Dispatchers.Main) {
                    (activity as MainActivity).goToHomeFragment()
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Toast.makeText(context, "Error creating sale ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createOnlineSaleObject(): Sale {
        val isOnline = true
        val currentTime = Calendar.getInstance().time.toString()
        val name = saleNameEditText.text.toString()
        val currentUserEmail = CredentialsManager.getInstance(context!!).loadUser()!!.first
        val link = linkEditText.text.toString()
        val thisSale: Sale
        if (name.isBlank() || normalPriceInput.text.toString().isBlank() || offerPriceInput.text.toString().isBlank() || link.isNullOrBlank()) {
            Toast.makeText(context, "You must fill all the fields", Toast.LENGTH_SHORT).show()
            thisSale = Sale(
                name = "",
                description = "",
                originalPrice = 0,
                salePrice = 0,
                isOnline = isOnline,
                createdAt = currentTime,
                categoryId = category,
                link = null,
                userEmail = currentUserEmail,
                photoUri = null,
                latitude = null,
                longitude = null
            )
        } else {
            val normalPrice = normalPriceInput.text.toString().toInt()
            val offerPrice = offerPriceInput.text.toString().toInt()
            if (offerPrice >= normalPrice || offerPrice < 0 || normalPrice < 0){
                if (offerPrice < 0 || normalPrice < 0){
                    Toast.makeText(context, "Both prices should be positive", Toast.LENGTH_SHORT).show()
                } else if (offerPrice >= normalPrice) {
                    Toast.makeText(context, "The offer price must be lower than the original price", Toast.LENGTH_SHORT).show()
                }
                thisSale = Sale(
                    name = "",
                    description = "",
                    originalPrice = 0,
                    salePrice = 0,
                    isOnline = isOnline,
                    createdAt = currentTime,
                    categoryId = category,
                    link = null,
                    userEmail = currentUserEmail,
                    photoUri = null,
                    latitude = null,
                    longitude = null
                )
            } else {
                thisSale = Sale(
                    name = name,
                    description = "",
                    originalPrice = normalPrice,
                    salePrice = offerPrice,
                    isOnline = isOnline,
                    createdAt = currentTime,
                    categoryId = category,
                    link = link,
                    userEmail = currentUserEmail,
                    photoUri = null,
                    latitude = null,
                    longitude = null
                )
            }
        }
        return thisSale
    }

}
