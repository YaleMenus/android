package com.adisa.diningplus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.adisa.diningplus.R
import com.adisa.diningplus.network.entities.Item
import com.adisa.diningplus.network.entities.Meal
import com.adisa.diningplus.utils.DateFormatProvider
import java.text.ParseException
import java.util.*

class MenuAdapter(private val context: Context, var meals: List<Meal>,
                  var items: HashMap<String, List<Item>>) : BaseExpandableListAdapter() {

    // TODO: make this a setter
    fun updateItems(items: HashMap<String, List<Item>>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getChild(mealPosition: Int, itemPosition: Int): Any {
        return items[meals[mealPosition].name]!![itemPosition]
    }

    override fun getChildId(mealPosition: Int, itemPosition: Int): Long {
        return itemPosition.toLong()
    }

    override fun getChildView(mealPosition: Int, itemPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val item = getChild(mealPosition, itemPosition) as Item
        if (convertView == null) {
            val layoutInflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.menu_item, null)
        }
        val expandedListTextView: TextView = convertView!!.findViewById(R.id.expandedListItem)
        expandedListTextView.text = item.name.replace('`', '\'')
        if (item.restricted) {
            expandedListTextView.setBackgroundColor(context.resources.getColor(R.color.fail))
            expandedListTextView.setTextColor(context.resources.getColor(R.color.colorMarked))
        } else {
            expandedListTextView.setBackgroundColor(context.resources.getColor(R.color.transparent))
            expandedListTextView.setTextColor(context.resources.getColor(R.color.primary))
        }
        return convertView
    }

    override fun getChildrenCount(mealPosition: Int): Int {
        return items[meals[mealPosition].name]!!.size
    }

    override fun getGroup(mealPosition: Int): Any {
        return meals[mealPosition]
    }

    override fun getGroupCount(): Int {
        return meals.size
    }

    override fun getGroupId(mealPosition: Int): Long {
        return mealPosition.toLong()
    }

    override fun getGroupView(mealPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val meal = getGroup(mealPosition) as Meal
        if (convertView == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.meal, null)
        }
        val listTitleTextView: TextView = convertView!!.findViewById(R.id.listTitle)
        listTitleTextView.text = meal.name
        listTitleTextView.setTextColor(context.resources.getColor(R.color.primary))
        val mealTimeTextView: TextView = convertView.findViewById(R.id.mealTime)
        var startTime: Date? = null
        var endTime: Date? = null
        // TODO: support 24 and 12 hour time
        try {
            startTime = DateFormatProvider.hour24.parse(meal.startTime)
            endTime = DateFormatProvider.hour24.parse(meal.endTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        mealTimeTextView.text = DateFormatProvider.hour.format(startTime) + "–" + DateFormatProvider.hour.format(endTime)
        val groupIndicator: ImageView = convertView.findViewById(R.id.help_group_indicator)
        if (isExpanded) {
            groupIndicator.setImageResource(R.drawable.ic_expand_less_black_24dp)
        } else {
            groupIndicator.setImageResource(R.drawable.ic_expand_more_black_24dp)
        }
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(mealPosition: Int, itemPosition: Int): Boolean {
        return true
    }

}