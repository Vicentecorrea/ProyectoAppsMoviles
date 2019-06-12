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
import com.example.salit.db.models.Sale
import kotlinx.android.synthetic.main.fragment_create_sale.*

class CreateSaleFragment : Fragment() {

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
        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val text = spinnerCategories.selectedItem.toString()
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context!!, text, duration)
                toast.show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

    private fun setSpinnerCategories() {
        val spinnerArray = ArrayAdapter(context!!, R.layout.simple_spinner_dropdown_item, Constants.CATEGORY_ARRAY)
        spinnerArray.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinnerCategories!!.adapter = spinnerArray
    }


//    private fun createSale() {
//        val saleObject = createSaleObject()
//
//    }

//    private fun createSaleObject(): Sale {
//        val name = saleNameEditText.text.toString()
//        val description = saleDescriptionEditText.text.toString()
//        var normalPrice = normalPriceInput.text.toString().toInt()
//        var offerPrice = offerPriceInput.text.toString().toInt()
//        return Sale(name = name, description = description, originalPrice = normalPrice, salePrice = offerPrice)
//    }

}
