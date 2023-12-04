package com.example.pmsmes.Adapter;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;

import com.example.pmsmes.Controller.EditTask_Activity;
import com.example.pmsmes.ItemAdapter.ItemStage;
import com.example.pmsmes.ItemAdapter.Item_Task;
import com.example.pmsmes.ItemAdapter.Stage;
import com.example.pmsmes.ItemAdapter.Tag;
import com.example.pmsmes.ItemAdapter.Task;
import com.example.pmsmes.ItemAdapter.User;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import android.widget.PopupMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterStage extends RecyclerView.Adapter<AdapterStage.MyViewHolder> implements AdapterTask.OnTaskItemClickListener, ItemMoveStage.ItemTouchHelperContract {
    ArrayList<Stage> itemStage = new ArrayList<>();
    ArrayList<Task> itemTask = new ArrayList<>();
    AdapterTask adapterTask;
    private Context context;
    private APIInterface apiServices;

    private String m_Text = "";

    private int currentPos = 0;
    public AdapterStage(ArrayList<Stage> itemStage,ArrayList<Task> itemTask, Context context) {
        this.itemStage = itemStage;
        this.context = context;
        this.itemTask = itemTask;
        apiServices = APIClient.getClient().create(APIInterface.class);

    }



    public AdapterStage(Context context) {
        this.context = context;
    }

    @Override
    //SelectItemTask
    public void onTaskItemClick(int stagePosition, int taskPosition) {
        Intent edtTask = new Intent(context, EditTask_Activity.class);
        edtTask.putExtra("projectID",itemStage.get(0).getProject());
        ArrayList<Task> getIdTask = new ArrayList<>();
        for(Task task : itemTask){
           if(task.getStage().equals(itemStage.get(stagePosition-1).getId())){
               getIdTask.add(task);
           }
        }
        edtTask.putExtra("stageID",itemStage.get(stagePosition-1).getId());
        edtTask.putExtra("taskID",getIdTask.get(taskPosition).getId());
        edtTask.putExtra("taskName",getIdTask.get(taskPosition).getName());
        context.startActivity(edtTask.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stage, parent, false);
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
        Stage item = itemStage.get(position);
        Log.d("aki", String.valueOf(position));
        currentPos = position;
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerViewTasks.getContext(),
                LinearLayoutManager.VERTICAL,false);
        holder.recyclerViewTasks.setLayoutManager(layoutManager);

        holder.edt_newTask.setVisibility(View.GONE);
        holder.btn_cancel.setVisibility(View.GONE);
        holder.btn_save.setVisibility(View.GONE);


        updateRecyclerViewTask(itemTask,position,holder);

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


        //Tạo mới task
        holder.btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTask = holder.edt_newTask.getText().toString();
                if (!newTask.isEmpty()) {
                    User user = new User();
                    user.setId(APIClient.getUserID(context));
                    user.setName(APIClient.getLoggedinName(context));
                    Task task = new Task();
                    task.setName(newTask);
                    task.setCreator(user);
                    task.setStage(itemStage.get(position).getId());

                    apiServices.createNewTask(APIClient.getToken(context),
                            itemStage.get(position).getProject(),
                            task.getName(),
                            task.getCreator().getId(),
                            task.getStage()).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            if (response.isSuccessful()){
                                itemTask.add(task);
                                updateRecyclerViewTask(itemTask,position,holder);
                                Toast.makeText(context, "Created new task", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(context, "Failed to new task", Toast.LENGTH_SHORT).show();
                        }
                    });

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
        holder.textV_name.setText(item.getName());
    }
    private void updateRecyclerViewTask(ArrayList<Task> taskList, int position, MyViewHolder holder) {
        //theo task tuong ung voi stage
//        Task task = new Task();
//        task.setName(taskName);
//        itemTask.add(task);

        ArrayList<Task> sortTaskList = new ArrayList<>();
        for (Task t: taskList) {
            if (t.getStage().equals(itemStage.get(position).getId())){
                sortTaskList.add(t);
            }
        }

        adapterTask = new AdapterTask(sortTaskList,position+1);
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
                    JsonObject stageRequest = new JsonObject ();
                    stageRequest.addProperty("stage" , itemStage.get(position).getId());
                    logData(stageRequest);
                    apiServices.removeProjectStage(APIClient.getToken(view.getContext()),
                            itemStage.get(position).getProject(),stageRequest).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(view.getContext(),"Stage deleted.",Toast.LENGTH_SHORT).show();
                                itemStage.remove(position);
                                notifyItemRemoved(position);
                            }
                        }
                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(view.getContext(),"Failed to delete.",Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                if (itemId == R.id.menu_item_EditStage){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Rename stage");

                    final EditText input = new EditText(context);

                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                    input.setText(itemStage.get(currentPos).getName());
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Text = input.getText().toString();
                            Toast.makeText(context,String.valueOf(currentPos),Toast.LENGTH_SHORT).show();
                            Stage c = itemStage.get(currentPos);
                            if (!TextUtils.isEmpty(m_Text)){
                                c.setName(input.getText().toString());
                                JsonObject updateStage = new JsonObject();
                                updateStage.addProperty("stage", c.getId());
                                updateStage.addProperty("name", c.getName());
                                apiServices.updateProjectStage(APIClient.getToken(context),
                                        c.getProject(), updateStage).enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(context, "Stage name changed", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {

                                    }
                                });
                            }


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                return true;
            }
        });
        popupMenu.show();
    }


    private void logData(Object object){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJsonString = gson.toJson(object);
        Log.d("Akkii", prettyJsonString);
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
