package com.adisa.diningplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adisa.diningplus.R;
import com.adisa.diningplus.activities.ItemActivity;

import java.util.ArrayList;

public class NutritionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemActivity.Nutrient> nutrients;

    class ViewHolder {
        TextView name;
        TextView amount;
        TextView pdv;
    }

    public NutritionAdapter(Context context, ArrayList<ItemActivity.Nutrient> nutrients) {
        this.context = context;
        this.nutrients = nutrients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        ItemActivity.Nutrient allergen = nutrients.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.nutrient, null);

            viewHolder.name = (TextView) convertView.findViewById(R.id.nutrient_name);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.nutrient_amount);
            viewHolder.pdv = (TextView) convertView.findViewById(R.id.nutrient_pdv);

            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(allergen.name);
        viewHolder.amount.setText(allergen.amount);
        if (allergen.pdv != null) {
            viewHolder.pdv.setText(allergen.pdv + "%");
        } else {
            viewHolder.pdv.setText("");
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return nutrients.size();
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