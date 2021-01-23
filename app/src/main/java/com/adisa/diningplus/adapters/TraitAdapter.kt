package com.adisa.diningplus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.adisa.diningplus.R
import com.adisa.diningplus.activities.ItemActivity.Trait
import java.util.*

class TraitAdapter(private val context: Context, private val traits: ArrayList<Trait>) : BaseAdapter() {

    internal inner class ViewHolder {
        var image: ImageView? = null
        var name: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewHolder: ViewHolder
        val trait = traits[position]
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView = inflater.inflate(R.layout.allergen, null)
            viewHolder.image = convertView.findViewById<View>(R.id.trait_img) as ImageView
            viewHolder.name = convertView.findViewById<View>(R.id.trait_text) as TextView
            convertView.isEnabled = false
            convertView.setOnClickListener(null)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.image!!.setImageDrawable(context.resources.getDrawable(trait.image))
        viewHolder.name!!.text = trait.name
        return convertView!!
    }

    override fun getCount(): Int {
        return traits.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}