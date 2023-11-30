package com.example.pmsmes.Adapter;
import com.example.pmsmes.ItemAdapter.Tag;
import com.example.pmsmes.ItemAdapter.Task;
import com.example.pmsmes.ItemAdapter.User;
import com.example.pmsmes.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmsmes.ItemAdapter.Item_Task;
import com.example.pmsmes.Utils.APIClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterTask extends RecyclerView.Adapter<AdapterTask.ViewHolder> implements ItemMoveTask.ItemTouchHelperContract {
    private Context context;
    private int layoutItem;
    private ArrayList<Task> itemTaskList;
    private OnTaskItemClickListener onTaskItemClickListener;
    private int stagePosition;

    public AdapterTask(Context context, int layoutItem, ArrayList<Task> itemTaskList, int stagePosition) {
        this.context = context;
        this.layoutItem = layoutItem;
        this.itemTaskList = itemTaskList;
        this.stagePosition = stagePosition;
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemTaskList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemTaskList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(AdapterStage.MyViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(AdapterStage.MyViewHolder myViewHolder) {

    }

    public interface OnTaskItemClickListener {
        void onTaskItemClick(int stagePosition, int taskPosition);
    }
    public void setOnTaskItemClickListener(OnTaskItemClickListener listener) {
        this.onTaskItemClickListener = listener;
    }

    public AdapterTask( ArrayList<Task> itemTaskList,int stagePosition) {
        this.itemTaskList = itemTaskList;
        this.stagePosition = stagePosition;
    }
    public AdapterTask(Context context, ArrayList<Task> itemTaskList) {
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
        Task itemTask = itemTaskList.get(position);
        holder.tvName.setText(itemTask.getName());
        ///AdapterTask adapter = new AdapterTask(context, layoutItem, itemTaskList, stagePosition);
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
