package com.adisa.diningplus.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.adisa.diningplus.R
import com.adisa.diningplus.activities.ItemActivity
import com.adisa.diningplus.activities.ItemActivity.Nutrient
import java.util.*

class NutritionAdapter(private val context: Context, private val nutrients: ArrayList<Nutrient>) : BaseAdapter() {

    internal inner class ViewHolder {
        var indent: View? = null
        var name: TextView? = null
        var amount: TextView? = null
        var pdv: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewHolder: ViewHolder
        val nutrient = nutrients[position]
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView = inflater.inflate(R.layout.nutrient, null)
            viewHolder.indent = convertView.findViewById(R.id.nutrient_indent)
            viewHolder.name = convertView.findViewById(R.id.nutrient_name)
            viewHolder.amount = convertView.findViewById(R.id.nutrient_amount)
            viewHolder.pdv = convertView.findViewById(R.id.nutrient_pdv)
            convertView.isEnabled = false
            convertView.setOnClickListener(null)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.indent!!.visibility =  if (nutrient.type == ItemActivity.NutrientType.SUB) View.VISIBLE else View.GONE
        viewHolder.name!!.text = nutrient.name
        if (nutrient.type == ItemActivity.NutrientType.SUB || nutrient.type == ItemActivity.NutrientType.PLAIN) {
            viewHolder.name!!.typeface = Typeface.DEFAULT
        }
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