package com.adisa.diningplus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.adisa.diningplus.R
import com.adisa.diningplus.activities.ItemActivity.Nutrient
import java.util.*

class NutritionAdapter(private val context: Context, private val nutrients: ArrayList<Nutrient>) : BaseAdapter() {

    internal inner class ViewHolder {
        var name: TextView? = null
        var amount: TextView? = null
        var pdv: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewHolder: ViewHolder
        val nutrient = nutrients[position]
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView = inflater.inflate(R.layout.nutrient, null)
            viewHolder.name = convertView.findViewById<View>(R.id.nutrient_name) as TextView
            viewHolder.amount = convertView.findViewById<View>(R.id.nutrient_amount) as TextView
            viewHolder.pdv = convertView.findViewById<View>(R.id.nutrient_pdv) as TextView
            convertView.isEnabled = false
            convertView.setOnClickListener(null)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.name!!.text = nutrient.name
        viewHolder.amount!!.text = nutrient.amount
        if (nutrient.pdv != null) {
            viewHolder.pdv!!.text = nutrient.pdv.toString() + "%"
        } else {
            viewHolder.pdv!!.text = ""
        }
        return convertView!!
    }

    override fun getCount(): Int {
        return nutrients.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}