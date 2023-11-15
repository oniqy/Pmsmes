package com.example.pmsmes.Adapter;
import com.example.pmsmes.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pmsmes.ItemAdapter.Item_Task;

import java.util.ArrayList;
public class AdapterTask extends ArrayAdapter{
    Context context;
    int layoutItem;
    ArrayList<Item_Task> ItemTask;

    public AdapterTask(@NonNull Context context, int resource, @NonNull ArrayList<Item_Task> ItemTask) {
        super(context, resource, ItemTask);
        this.context = context;
        this.layoutItem = resource;
        this.ItemTask = ItemTask;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item_Task task =ItemTask.get(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(layoutItem,null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_nameTask);
        tvName.setText(task.getName());

        return convertView;
    }
}
