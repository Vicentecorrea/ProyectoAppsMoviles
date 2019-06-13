package com.example.salit.fragments

import android.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.salit.Constants
import com.example.salit.db.AppDatabase
import com.example.salit.db.models.Sale
import kotlinx.android.synthetic.main.fragment_create_sale.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class CreateSaleFragment : Fragment() {

    private var category = "All"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.salit.R.layout.fragment_create_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSpinnerCategories()
        addSpinnerCategoryListener()
        createSaleButton.setOnClickListener {
            createSale()
        }
    }

    private fun addSpinnerCategoryListener() {
        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = spinnerCategories.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setSpinnerCategories() {
        val spinnerArray = ArrayAdapter(context!!, R.layout.simple_spinner_dropdown_item, Constants.CATEGORY_ARRAY)
        spinnerArray.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinnerCategories!!.adapter = spinnerArray
    }


    private fun createSale() {
        val saleObject = createSaleObject()
        storeSaleObject(saleObject)

    }

    private fun storeSaleObject(saleObject: Sale) {
        val saleDao = AppDatabase.getDatabase(context!!).SaleDao()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                saleDao.insertAll(saleObject)
                launch(Dispatchers.Main) {
                    Toast.makeText(context, "Sale saved successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception){
                launch(Dispatchers.Main) {
                    Toast.makeText(context, "Error creating sale ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createSaleObject(): Sale {
        val name = saleNameEditText.text.toString()
        val description = saleDescriptionEditText.text.toString()
        val normalPrice = normalPriceInput.text.toString().toInt()
        val offerPrice = offerPriceInput.text.toString().toInt()
        val isOnline = checkBoxIsOnline.isChecked
        val currentTime = Calendar.getInstance().time.toString()
        return Sale(name = name, description = description, originalPrice = normalPrice, salePrice = offerPrice, isOnline = isOnline, createdAt = currentTime, category = category)
    }

}
