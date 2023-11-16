package com.example.pmsmes.Adapter;

import com.example.pmsmes.ItemAdapter.ItemStage;
import com.example.pmsmes.ItemAdapter.Item_Task;
import com.example.pmsmes.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class AdapterStage extends RecyclerView.Adapter<AdapterStage.MyViewHolder>{
    ArrayList<ItemStage> itemStage = new ArrayList<>();
    ArrayList<Item_Task> itemTask = new ArrayList<>();
    AdapterTask adapterTask;
    public AdapterStage(ArrayList<ItemStage> itemStage) {
        this.itemStage = itemStage;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stage, parent, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        itemView.setLayoutParams(params);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemStage item = itemStage.get(position);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerViewTasks.getContext(),LinearLayoutManager.VERTICAL,false);
        holder.recyclerViewTasks.setLayoutManager(layoutManager);

        ArrayList<String> list = new ArrayList<String>();
        // thêm các phần tử vào list
        list.add("Java");
        list.add("C++");
        list.add("C++++");

        itemTask.clear();
        for (int i = 0;i<list.size();i++){
            Item_Task task = new Item_Task();
            task.name = list.get(i);
            itemTask.add(task);
        };
        adapterTask = new AdapterTask(itemTask);
        adapterTask.notifyDataSetChanged();
        holder.textV_name.setText(String.valueOf(item.getTvStageName()));
        holder.recyclerViewTasks.setAdapter(adapterTask);
    }
    @Override
    public int getItemCount() {
        return itemStage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textV_name;
        public RecyclerView recyclerViewTasks;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textV_name = (TextView) itemView.findViewById(R.id.tvStageName);
            recyclerViewTasks = itemView.findViewById(R.id.recyclerViewTasks);
        }
    }
}
