package com.adisa.diningplus.adapters

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.adisa.diningplus.R
import com.adisa.diningplus.network.entities.Hall
import java.text.DecimalFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainListAdapter(private val context: Context) : BaseAdapter() {
    private val preferences: SharedPreferences
    private val shieldMap = HashMap<String, Int>()
    private val occupancyImages: Array<Int>
    private var halls = ArrayList<Hall>()
    private val hallSort = Comparator<Hall> { h1, h2 -> h1.distance.compareTo(h2.distance) }

    internal inner class ViewHolder {
        var shield: ImageView? = null
        var name: TextView? = null
        var distance: TextView? = null
        var occupancy: ImageView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewHolder: ViewHolder
        val item = getItem(position)
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView = inflater.inflate(R.layout.hall_list, null)
            viewHolder.shield = convertView.findViewById(R.id.shield)
            viewHolder.name = convertView.findViewById(R.id.hall_name)
            viewHolder.distance = convertView.findViewById(R.id.hall_distance)
            viewHolder.occupancy = convertView.findViewById(R.id.hall_occupancy)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        val shieldId = shieldMap[item.id]
        if (shieldId != null) {
            viewHolder.shield!!.setImageDrawable(context.resources.getDrawable(shieldId))
        } else {
            viewHolder.shield!!.setImageDrawable(context.resources.getDrawable(R.drawable.commons))
        }
        viewHolder.name!!.text = item.nickname
        var distance = item.distance
        var numberFormat = DecimalFormat("0")
        var unit = ""
        when (preferences.getString("units", "Imperial")) {
            "Metric" -> {
                if (distance < 1) {
                    distance *= 1000
                    unit += "m"
                } else {
                    unit += "km"
                }
            }
            "Imperial" -> {
                distance *= 0.621371
                if (distance < 0.15) {
                    distance *= 5280
                    unit += "ft"
                } else {
                    if (distance < 5) {
                        numberFormat = DecimalFormat("0.0")
                    }
                    unit += "mi"
                }
            }
        }
        viewHolder.distance!!.text = numberFormat.format(distance) + unit
        viewHolder.occupancy!!.setImageDrawable(context.resources.getDrawable(occupancyImages[item.occupancy]))
        // Gray out closed halls
        convertView!!.alpha = if (item.open) 1f else 0.4f
        return convertView
    }

    fun setLists(openHalls: ArrayList<Hall>, closedHalls: ArrayList<Hall>) {
        this.halls = ArrayList()
        Collections.sort(openHalls, hallSort)
        Collections.sort(closedHalls, hallSort)
        this.halls.addAll(openHalls)
        this.halls.addAll(closedHalls)
    }

    override fun getCount(): Int {
        return halls.size
    }

    override fun getItem(position: Int): Hall {
        return halls[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        shieldMap["BK"] = R.drawable.berkeley
        shieldMap["BR"] = R.drawable.branford
        shieldMap["GH"] = R.drawable.hopper
        shieldMap["ES"] = R.drawable.stiles
        shieldMap["DC"] = R.drawable.davenport
        shieldMap["BF"] = R.drawable.franklin
        shieldMap["MY"] = R.drawable.murray
        shieldMap["JE"] = R.drawable.je
        shieldMap["MC"] = R.drawable.morse
        shieldMap["PC"] = R.drawable.pierson
        shieldMap["SY"] = R.drawable.saybrook
        shieldMap["SM"] = R.drawable.silliman
        shieldMap["TD"] = R.drawable.td
        shieldMap["TC"] = R.drawable.trumbull
        occupancyImages = arrayOf(
            R.drawable.occupancy0,
            R.drawable.occupancy1,
            R.drawable.occupancy2,
            R.drawable.occupancy3,
            R.drawable.occupancy4,
            R.drawable.occupancy5,
            R.drawable.occupancy6,
            R.drawable.occupancy7,
            R.drawable.occupancy8,
            R.drawable.occupancy9,
            R.drawable.occupancy10,
        )
    }
}