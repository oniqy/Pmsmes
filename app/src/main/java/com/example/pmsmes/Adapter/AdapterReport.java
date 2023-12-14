package com.example.pmsmes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pmsmes.ItemAdapter.Item_Report;
import com.example.pmsmes.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterReport extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private int layoutItem;
    ArrayList<Item_Report> item_reports = new ArrayList<>();

    @Override
    public int getCount() {
        return item_reports.size();
    }

    public AdapterReport(Activity context, int layoutItem, ArrayList<Item_Report> item_reports) {
        this.layoutInflater = context.getLayoutInflater();
        this.layoutItem = layoutItem;
        this.item_reports = item_reports;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    static class ViewHolder{
        TextView tv_info,tv_count;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_gridview_report, null);
            holder = new ViewHolder();
            holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
            holder.tv_count = (TextView) view.findViewById(R.id.tv_count);

            // Set the holder as a tag to the view
            view.setTag(holder);
        } else {
            // If the view is recycled, get the holder from the tag
            holder = (ViewHolder) view.getTag();
        }

        // Now you can set the data to the views using the holder
        Item_Report currentItem = item_reports.get(i);
        holder.tv_info.setText(currentItem.getInfo());
        holder.tv_count.setText(String.valueOf(currentItem.getCount_rs()));

        return view;
    }

}
