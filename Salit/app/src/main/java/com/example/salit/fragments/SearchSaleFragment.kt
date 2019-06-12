package com.example.salit.fragments


import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_create_sale.*
import kotlinx.android.synthetic.main.fragment_search_sale.*


class SearchSaleFragment : Fragment() {

    private var currentCategory: String = Constants.CATEGORY_ARRAY[0]

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setSpinnerCategories()

    }

    private fun setSpinnerCategories(){
        val spinnerArray = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, Constants.CATEGORY_ARRAY)
        spinnerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerArray
    }

    private fun setListeners(){
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentCategory = Constants.CATEGORY_ARRAY[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }



}
