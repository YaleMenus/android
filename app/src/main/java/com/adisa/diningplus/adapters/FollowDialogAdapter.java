package com.adisa.diningplus.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adisa.diningplus.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Adisa on 5/4/2017.
 */

public class FollowDialogAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> stringList = new ArrayList<>();
    private SharedPreferences preferences;

    private class ViewHolder {
        TextView name;
        ImageButton button;
    }

    public FollowDialogAdapter(Context context, HashSet<String> stringSet) {
        this.context = context;
        for (String string : stringSet) {
            stringList.add(string);
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        final String item = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.follow_dialog_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.followItemName);
            viewHolder.button = (ImageButton) convertView.findViewById(R.id.followItemRemove);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(item);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashSet<String> currentSet = (HashSet<String>) preferences.getStringSet("followedItems", new HashSet<String>());
                HashSet<String> newSet = new HashSet<String>();
                newSet.addAll(currentSet);
                newSet.remove(item);
                stringList.remove(item);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putStringSet("followedItems", newSet);
                editor.apply();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public String getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

