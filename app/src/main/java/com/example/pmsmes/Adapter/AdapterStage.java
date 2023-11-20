package com.example.pmsmes.Adapter;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;

import com.example.pmsmes.Controller.EditTask_Activity;
import com.example.pmsmes.ItemAdapter.ItemStage;
import com.example.pmsmes.ItemAdapter.Item_Task;
import com.example.pmsmes.R;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import android.widget.PopupMenu;
public class AdapterStage extends RecyclerView.Adapter<AdapterStage.MyViewHolder> implements AdapterTask.OnTaskItemClickListener, ItemMoveStage.ItemTouchHelperContract {
    ArrayList<ItemStage> itemStage = new ArrayList<>();
    ArrayList<ArrayList<Item_Task>> itemTask = new ArrayList<>();
    AdapterTask adapterTask;
    private Context context;

    public AdapterStage(ArrayList<ItemStage> itemStage, Context context) {
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
    //SelectItemTask
    public void onTaskItemClick(int stagePosition, int taskPosition) {
        Intent edtTask = new Intent(context, EditTask_Activity.class);
        context.startActivity(edtTask.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
    public void onColumnMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemStage, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemStage, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onColumnSelected(MyViewHolder myViewHolder) {

    }

    @Override
    public void onColumnClear(MyViewHolder myViewHolder) {

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        holder.btn_optionStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsStage(v,position);
            }
        });
        holder.textV_name.setText(String.valueOf(item.getTvStageName()));
    }
    private void updateRecyclerViewTask(String taskName, int position, MyViewHolder holder) {
        //theo task tuong ung voi stage
        Item_Task task = new Item_Task();
        task.name = taskName;
        itemTask.get(position).add(task);
        adapterTask = new AdapterTask(itemTask.get(position),position);
        adapterTask.setOnTaskItemClickListener(this);
        adapterTask.notifyDataSetChanged();
        //Move Item Task
        ItemTouchHelper.Callback callback =
                new ItemMoveTask(adapterTask);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(holder.recyclerViewTasks);
        holder.recyclerViewTasks.setAdapter(adapterTask);
        holder.edt_newTask.setText("");
    }
    @SuppressLint("ResourceType")
    private void showOptionsStage(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.option_stage, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_item_removeStage) {
                    itemStage.remove(position);
                    notifyItemRemoved(position);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return itemStage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textV_name;
        public RecyclerView recyclerViewTasks;
        public Button btn_addTask,btn_cancel,btn_save;
        public ImageButton btn_optionStage;
        public EditText edt_newTask;
        public View rowView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
            btn_optionStage = (ImageButton) itemView.findViewById(R.id.btn_optionStage);
            textV_name = (TextView) itemView.findViewById(R.id.tvStageName);
            edt_newTask = (EditText) itemView.findViewById(R.id.edt_newTask);
            recyclerViewTasks = (RecyclerView) itemView.findViewById(R.id.recyclerViewTasks);
            btn_addTask =(Button) itemView.findViewById(R.id.btn_addTask);
            btn_cancel =(Button) itemView.findViewById(R.id.btn_cancel);
            btn_save =(Button) itemView.findViewById(R.id.btn_save);
        }
    }
}
