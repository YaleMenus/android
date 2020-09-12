package com.adisa.diningplus.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adisa.diningplus.R;
import com.adisa.diningplus.activities.MainActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Adisa on 4/27/2017.
 */

public class MainListAdapter extends BaseAdapter {
    private Context context;
    private SharedPreferences preferences;
    private HashMap<String, Integer> shieldMap = new HashMap<>();
    private ArrayList<MainActivity.LocationItem> locations = new ArrayList<>();
    private ArrayList<MainActivity.LocationItem> openLocations = new ArrayList<>();
    private ArrayList<MainActivity.LocationItem> closedLocations = new ArrayList<>();
    private Comparator<MainActivity.LocationItem> hallSort = new Comparator<MainActivity.LocationItem>() {
        @Override
        public int compare(MainActivity.LocationItem o1, MainActivity.LocationItem o2) {
            return Double.compare(o1.distance, o2.distance);
        }
    };

    class ViewHolder {
        ImageView shield;
        TextView name;
        TextView distance;
        TextView occupancy;
    }

    public MainListAdapter(Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        shieldMap.put("Berkeley", R.drawable.berkeley);
        shieldMap.put("Branford", R.drawable.branford);
        shieldMap.put("Grace Hopper", R.drawable.hopper);
        shieldMap.put("Stiles", R.drawable.stiles);
        shieldMap.put("Davenport", R.drawable.davenport);
        shieldMap.put("Franklin", R.drawable.franklin);
        shieldMap.put("Pauli Murray", R.drawable.murray);
        shieldMap.put("Jonathan Edwards", R.drawable.je);
        shieldMap.put("Morse", R.drawable.morse);
        shieldMap.put("Pierson", R.drawable.pierson);
        shieldMap.put("Saybrook", R.drawable.saybrook);
        shieldMap.put("Silliman", R.drawable.silliman);
        shieldMap.put("Timothy Dwight", R.drawable.td);
        shieldMap.put("Trumbull", R.drawable.trumbull);
    }

    @Override
    public void notifyDataSetChanged() {
        locations = new ArrayList<>();
        Collections.sort(openLocations, hallSort);
        Collections.sort(closedLocations, hallSort);
        locations.addAll(openLocations);
        locations.addAll(closedLocations);
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        MainActivity.LocationItem item = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.hall_list, null);
            viewHolder.shield = (ImageView) convertView.findViewById(R.id.shield);
            viewHolder.name = (TextView) convertView.findViewById(R.id.hall_name);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.hall_distance);
            viewHolder.occupancy = (TextView) convertView.findViewById(R.id.hall_occupancy);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Integer shieldId = shieldMap.get(item.name);
        if (shieldId != null) {
            viewHolder.shield.setImageDrawable(context.getResources().getDrawable(shieldId));
        } else {
            viewHolder.shield.setImageDrawable(context.getResources().getDrawable(R.drawable.commons));
        }
        viewHolder.name.setText(item.name);

        DecimalFormat numberFormat = new DecimalFormat("0.00");
        double distance = item.distance;
        String unit = " ";
        switch (preferences.getString("unitPrefs", "Imperial")) {
            case "Metric":
                unit += "km";
                // No adjustment of distance needed as it's stored in kilometers
                break;
            case "Imperial":
                distance *= 0.621371;
                unit += "mi";
                break;
        }
        if (distance > 50) {
            viewHolder.distance.setText("> 50" + unit);
        } else {
            viewHolder.distance.setText("" + numberFormat.format(distance) + unit);
        }
        int capacity = item.occupancy * 10;

        if (!item.open) {
            viewHolder.occupancy.setTextColor(Color.parseColor("#A8030303"));
        } else if (capacity >= 80) {
            viewHolder.occupancy.setTextColor(Color.parseColor("#d62b2b"));
        } else if (capacity >= 30) {
            viewHolder.occupancy.setTextColor(Color.parseColor("#eb9438"));
        } else {
            viewHolder.occupancy.setTextColor(Color.parseColor("#64dd17"));
        }
        viewHolder.occupancy.setText(item.open ? capacity + "%" : "Closed");
        // Gray out closed location
        convertView.setAlpha(item.open ? 1f : 0.4f);

        return convertView;
    }

    public void setLists(ArrayList<MainActivity.LocationItem> openLocations, ArrayList<MainActivity.LocationItem> closedLocations) {
        this.openLocations = openLocations;
        this.closedLocations = closedLocations;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public MainActivity.LocationItem getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
