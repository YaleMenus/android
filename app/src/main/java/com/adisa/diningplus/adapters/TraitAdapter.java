package com.adisa.diningplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adisa.diningplus.R;
import com.adisa.diningplus.activities.ItemActivity;

import java.util.ArrayList;

public class TraitAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemActivity.Trait> traits;

    class ViewHolder {
        ImageView image;
        TextView name;
    }

    public TraitAdapter(Context context, ArrayList<ItemActivity.Trait> traits) {
        this.context = context;
        this.traits = traits;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        ItemActivity.Trait trait = traits.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.allergen, null);

            viewHolder.image = (ImageView) convertView.findViewById(R.id.trait_img);
            viewHolder.name = (TextView) convertView.findViewById(R.id.trait_text);

            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.image.setImageDrawable(context.getResources().getDrawable(trait.image));
        viewHolder.name.setText(trait.name);

        return convertView;
    }

    @Override
    public int getCount() {
        return traits.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}