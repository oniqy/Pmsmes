package com.example.pmsmes.Adapter;
import com.example.pmsmes.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmsmes.ItemAdapter.Item_Task;

import java.util.ArrayList;
public class AdapterTask extends RecyclerView.Adapter<AdapterTask.ViewHolder>{
    private Context context;
    private int layoutItem;
    private ArrayList<Item_Task> itemTaskList;
    private OnTaskItemClickListener onTaskItemClickListener;
    private int stagePosition;

    public AdapterTask(Context context, int layoutItem, ArrayList<Item_Task> itemTaskList, int stagePosition) {
        this.context = context;
        this.layoutItem = layoutItem;
        this.itemTaskList = itemTaskList;
        this.stagePosition = stagePosition;
    }
    public interface OnTaskItemClickListener {
        void onTaskItemClick(int stagePosition, int taskPosition);
    }
    public void setOnTaskItemClickListener(OnTaskItemClickListener listener) {
        this.onTaskItemClickListener = listener;
    }

    public AdapterTask( ArrayList<Item_Task> itemTaskList,int stagePosition) {
        this.itemTaskList = itemTaskList;
        this.stagePosition = stagePosition;
    }
    public AdapterTask(Context context, ArrayList<Item_Task> itemTaskList) {
        this.context = context;
        this.itemTaskList = itemTaskList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item_Task itemTask = itemTaskList.get(position);
        holder.tvName.setText(itemTask.getName());
        AdapterTask adapter = new AdapterTask(context, layoutItem, itemTaskList, stagePosition);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int taskPosition = holder.getAdapterPosition();
                if (taskPosition != RecyclerView.NO_POSITION && onTaskItemClickListener != null) {
                    onTaskItemClickListener.onTaskItemClick(stagePosition, taskPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemTaskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_nameTask);
        }
    }
}
