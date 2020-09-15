package com.adisa.diningplus.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adisa.diningplus.db.entities.Item;
import com.adisa.diningplus.db.entities.Meal;
import com.adisa.diningplus.utils.DateFormatProvider;
import com.adisa.diningplus.activities.LocationActivity;
import com.adisa.diningplus.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adisa on 4/9/2017.
 */

public class MenuAdapter extends BaseExpandableListAdapter {
    private Context context;
    public List<Meal> meals;
    public HashMap<String, List<Item>> items;

    public MenuAdapter(Context context, List<Meal> meals,
                       HashMap<String, List<Item>> items) {
        this.context = context;
        this.meals = meals;
        this.items = items;
    }

    public void setItems(HashMap<String, List<Item>> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int mealPosition, int itemPosition) {
        return this.items.get(this.meals.get(mealPosition).name).get(itemPosition);
    }

    @Override
    public long getChildId(int mealPosition, int itemPosition) {
        return itemPosition;
    }

    @Override
    public View getChildView(int mealPosition, final int itemPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        Item item = (Item) getChild(mealPosition, itemPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.menu_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(item.name.replace('`', '\''));
        if (item.allergenic) {
            expandedListTextView.setBackgroundColor(context.getResources().getColor(R.color.backgroundMarked));
            expandedListTextView.setTextColor(context.getResources().getColor(R.color.colorMarked));
        } else {
            expandedListTextView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            expandedListTextView.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int mealPosition) {
        return this.items.get(this.meals.get(mealPosition).name).size();
    }

    @Override
    public Object getGroup(int mealPosition) {
        return this.meals.get(mealPosition);
    }

    @Override
    public int getGroupCount() {
        return this.meals.size();
    }

    @Override
    public long getGroupId(int mealPosition) {
        return mealPosition;
    }

    @Override
    public View getGroupView(int mealPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Meal meal = (Meal) getGroup(mealPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.meal, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setText(meal.name);
        listTitleTextView.setTextColor(this.context.getResources().getColor(R.color.colorPrimary));

        TextView mealTimeTextView = (TextView) convertView.findViewById(R.id.mealTime);
        Date startTime = null, endTime = null;
        // TODO: support 24 and 12 hour time
        try {
            startTime = DateFormatProvider.hour.parse(meal.startTime);
            endTime = DateFormatProvider.hour.parse(meal.endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mealTimeTextView.setText(DateFormatProvider.hour.format(startTime) + "–" + DateFormatProvider.hour.format(endTime));
        ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.help_group_indicator);
        if (isExpanded) {
            groupIndicator.setImageResource(R.drawable.ic_expand_less_black_24dp);
        } else {
            groupIndicator.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int mealPosition, int itemPosition) {
        return true;
    }
}
