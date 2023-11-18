package com.example.pmsmes.Adapter;
import android.content.Context;

import com.example.pmsmes.Controller.EditTask_Activity;
import com.example.pmsmes.ItemAdapter.ItemStage;
import com.example.pmsmes.ItemAdapter.Item_Task;
import com.example.pmsmes.R;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class AdapterStage extends RecyclerView.Adapter<AdapterStage.MyViewHolder> implements AdapterTask.OnTaskItemClickListener{
    ArrayList<ItemStage> itemStage = new ArrayList<>();
    ArrayList<ArrayList<Item_Task>> itemTask = new ArrayList<>();
    AdapterTask adapterTask;
    private Context context;
    public AdapterStage(ArrayList<ItemStage> itemStage,Context context) {
        this.itemStage = itemStage;
        this.context = context;
        for (int i = 0; i < itemStage.size(); i++) {
            itemTask.add(new ArrayList<>());
        }
    }

    public AdapterStage(Context context) {
        this.context = context;
    }
    @Override
    public void onTaskItemClick(int stagePosition, int taskPosition) {
        //SelectItemTask
        Intent edtTask = new Intent(context, EditTask_Activity.class);
        context.startActivity(edtTask);
        Toast.makeText(context, "Task clicked at position: " + taskPosition, Toast.LENGTH_SHORT).show();
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
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemStage item = itemStage.get(position);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerViewTasks.getContext(),
                LinearLayoutManager.VERTICAL,false);
        holder.recyclerViewTasks.setLayoutManager(layoutManager);

        holder.edt_newTask.setVisibility(View.GONE);
        holder.btn_cancel.setVisibility(View.GONE);
        holder.btn_save.setVisibility(View.GONE);

        holder.btn_addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.edt_newTask.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.VISIBLE);
                holder.btn_save.setVisibility(View.VISIBLE);
            }
        });
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.edt_newTask.setVisibility(View.GONE);
                holder.btn_cancel.setVisibility(View.GONE);
                holder.btn_save.setVisibility(View.GONE);
            }
        });
        holder.btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newTask = holder.edt_newTask.getText().toString();
                if (newTask != null && !newTask.isEmpty()) {
                    updateRecyclerViewTask(newTask,position,holder);
                    holder.edt_newTask.setVisibility(View.GONE);
                    holder.btn_cancel.setVisibility(View.GONE);
                    holder.btn_save.setVisibility(View.GONE);
                } else {
                    Toast.makeText(v.getContext(), "Please enter a task name",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        adapterTask = new AdapterTask(itemTask.get(position),position);
        adapterTask.setOnTaskItemClickListener(new AdapterTask.OnTaskItemClickListener() {
            @Override
            public void onTaskItemClick(int stagePosition, int taskPosition) {
                Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
            }
        });
        holder.textV_name.setText(String.valueOf(item.getTvStageName()));
    }
    private void updateRecyclerViewTask(String taskName, int position, MyViewHolder holder) {
        Item_Task task = new Item_Task();
        task.name = taskName;
        itemTask.get(position).add(task);
        adapterTask = new AdapterTask(itemTask.get(position),position);
        adapterTask.setOnTaskItemClickListener(this);
        adapterTask.notifyDataSetChanged();
        holder.recyclerViewTasks.setAdapter(adapterTask);
    }

    @Override
    public int getItemCount() {
        return itemStage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textV_name;
        public RecyclerView recyclerViewTasks;
        public Button btn_addTask,btn_cancel,btn_save;
        public EditText edt_newTask;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textV_name = (TextView) itemView.findViewById(R.id.tvStageName);
            edt_newTask = (EditText) itemView.findViewById(R.id.edt_newTask);
            recyclerViewTasks = (RecyclerView) itemView.findViewById(R.id.recyclerViewTasks);
            btn_addTask =(Button) itemView.findViewById(R.id.btn_addTask);
            btn_cancel =(Button) itemView.findViewById(R.id.btn_cancel);
            btn_save =(Button) itemView.findViewById(R.id.btn_save);
        }
    }
}
