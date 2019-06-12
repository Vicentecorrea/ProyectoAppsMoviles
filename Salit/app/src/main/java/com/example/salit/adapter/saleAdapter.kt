package com.example.salit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.salit.R
import com.example.salit.db.models.Sale
import java.text.FieldPosition

class saleAdapter(
    context: Context,
    private val dataSource: ArrayList<Sale>) : BaseAdapter(){

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int{
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.list_item_sale, parent, false)
        rowView.findViewById<TextView>(R.id.saleName).text = dataSource[position].name
        rowView.findViewById<TextView>(R.id.originalPrice).text = dataSource[position].originalPrice.toString()
        rowView.findViewById<TextView>(R.id.salePrice).text = dataSource[position].salePrice.toString()
        return rowView
    }
}