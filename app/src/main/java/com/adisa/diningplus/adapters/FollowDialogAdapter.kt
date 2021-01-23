package com.adisa.diningplus.adapters

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.adisa.diningplus.R
import java.util.*

class FollowDialogAdapter(private val context: Context, itemNameSet: MutableSet<String>) : BaseAdapter() {
    private val itemNames = ArrayList<String>()
    private val preferences: SharedPreferences

    private inner class ViewHolder {
        var name: TextView? = null
        var button: ImageButton? = null
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewHolder: ViewHolder
        val item = getItem(position)
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView = inflater.inflate(R.layout.follow_dialog_item, null)
            viewHolder.name = convertView.findViewById<View>(R.id.followItemName) as TextView
            viewHolder.button = convertView.findViewById<View>(R.id.followItemRemove) as ImageButton
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.name!!.text = item
        viewHolder.button!!.setOnClickListener {
            val currentSet = preferences.getStringSet("followed_items", HashSet()) as HashSet<String>?
            val newSet = HashSet<String>()
            newSet.addAll(currentSet!!)
            newSet.remove(item)
            itemNames.remove(item)
            val editor = preferences.edit()
            editor.putStringSet("followed_items", newSet)
            editor.apply()
            notifyDataSetChanged()
        }
        return convertView!!
    }

    override fun getCount(): Int {
        return itemNames.size
    }

    override fun getItem(position: Int): String {
        return itemNames[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    init {
        itemNames.addAll(itemNameSet);
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }
}