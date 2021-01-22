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

public class AllergenAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemActivity.Trait> allergens;

    class ViewHolder {
        ImageView image;
        TextView name;
    }

    public AllergenAdapter(Context context, ArrayList<ItemActivity.Trait> allergens) {
        this.context = context;
        this.allergens = allergens;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        ItemActivity.Trait allergen = allergens.get(position);
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
        viewHolder.image.setImageDrawable(context.getResources().getDrawable(allergen.image));
        viewHolder.name.setText(allergen.name);

        return convertView;
    }

    @Override
    public int getCount() {
        return allergens.size();
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