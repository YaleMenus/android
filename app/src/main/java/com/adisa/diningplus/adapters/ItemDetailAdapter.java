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

public class ItemDetailAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
/*
    private Context context;
    private static final int TYPE_HEADER = -1;
    private static final int TYPE_WARNING = -2;
    private static final int TYPE_ITEM = -3;
    private static final int TYPE_TRAIT = -4;
    private ArrayList<ItemActivity.Detail> detailList;

    class ViewHolder {
        ImageView image;
        TextView name;
    }

    public ItemDetailAdapter(Context context, ArrayList<ItemActivity.Detail> detailList) {
        this.context = context;
        this.detailList = detailList;
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
        ItemActivity.Detail item = detailList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (getItemViewType(position)) {
                case TYPE_HEADER:
                    convertView = inflater.inflate(R.layout.item_detail_header, null);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.subheader);
                    break;
                case TYPE_ITEM:
                    convertView = inflater.inflate(R.layout.item_detail_item, null);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.detail_item);
                    break;
                case TYPE_WARNING:
                    convertView = inflater.inflate(R.layout.item_detail_desc, null);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.desc);
                    break;
                case TYPE_TRAIT:
                    convertView = inflater.inflate(R.layout.item_detail_trait, null);
                    viewHolder.image = (ImageView) convertView.findViewById(R.id.trait_img);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.trait_text);
                    break;
            }
            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(item.name);
        if (getItemViewType(position) == TYPE_TRAIT)
            viewHolder.image.setImageDrawable(context.getResources().getDrawable(item.image));

        return convertView;
    }

    @Override
    public int getCount() {
        return detailList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        switch (detailList.get(position).image) {
            case TYPE_HEADER:
                return TYPE_HEADER;
            case TYPE_ITEM:
                return TYPE_ITEM;
            case TYPE_WARNING:
                return TYPE_WARNING;
            default:
                return TYPE_TRAIT;
        }
    }
 */
}
