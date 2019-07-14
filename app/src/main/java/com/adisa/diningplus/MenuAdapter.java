package com.adisa.diningplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adisa on 4/9/2017.
 */

class MenuAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<DiningHallActivity.Meal> expandableListTitle;
    private HashMap<String, ArrayList<DiningHallActivity.FoodItem>> expandableListDetail;

    MenuAdapter(Context context, List<DiningHallActivity.Meal> expandableListTitle,
                                       HashMap<String, ArrayList<DiningHallActivity.FoodItem>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    void setMap(HashMap<String, ArrayList<DiningHallActivity.FoodItem>> expandableListDetail){
        this.expandableListDetail = expandableListDetail;
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition).getName())
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        DiningHallActivity.FoodItem item = (DiningHallActivity.FoodItem) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.menu_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(item.getName());
        if (item.marked){
            expandedListTextView.setBackgroundColor(context.getResources().getColor(R.color.backgroundMarked));
            expandedListTextView.setTextColor(context.getResources().getColor(R.color.colorMarked));
        } else {
            expandedListTextView.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition).getName())
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        DiningHallActivity.Meal item = (DiningHallActivity.Meal) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.menu_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setText(item.getName());
        listTitleTextView.setTextColor(this.context.getResources().getColor(R.color.colorPrimary));

        TextView mealTimeTextView = (TextView) convertView.findViewById(R.id.mealTime);
        Date startTime = null, endTime = null;
        try {
            startTime = new SimpleDateFormat("HH:mm:ss").parse(item.getStartTime());
            endTime = new SimpleDateFormat("HH:mm:ss").parse(item.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mealTimeTextView.setText(new SimpleDateFormat("h:mm a").format(startTime) + "–" + new SimpleDateFormat("h:mm a").format(endTime));
        ImageView groupIndicator = (ImageView) convertView.findViewById(R.id.help_group_indicator);
        if (isExpanded){
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
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
