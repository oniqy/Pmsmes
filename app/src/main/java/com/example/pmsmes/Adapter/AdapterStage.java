package com.example.pmsmes.Adapter;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;

import com.example.pmsmes.Controller.EditTask_Activity;
import com.example.pmsmes.Controller.ProjectWorkspaceActivity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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
    AdapterStage adapterStage;

    RecyclerView stageRecycleView;
    private Context context;
    private APIInterface apiServices;

    private String m_Text = "";

    private int currentPos = 0;
    public AdapterStage(ArrayList<Stage> itemStage,ArrayList<Task> itemTask, Context context) {
        this.itemStage = itemStage;
        this.context = context;
        this.itemTask = itemTask;
        apiServices = APIClient.getClient().create(APIInterface.class);
        this.adapterStage = this;

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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        stageRecycleView = recyclerView;
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

        currentPos = position;
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerViewTasks.getContext(),
                LinearLayoutManager.VERTICAL,false);
        holder.recyclerViewTasks.setLayoutManager(layoutManager);

        stageRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    currentPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();;

                }
            }
        });


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
        currentPos = position;

        ArrayList<Task> sortTaskList = new ArrayList<>();
        for (Task t: taskList) {
            if (t.getStage().equals(itemStage.get(position).getId())){
                sortTaskList.add(t);
            }
        }

        adapterTask = new AdapterTask(sortTaskList,position+1);
        adapterTask.setOnTaskItemClickListener(this);
        //Move Item Task
        ItemTouchHelper.Callback callback =
                new ItemMoveTask(adapterTask);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(holder.recyclerViewTasks);
        holder.recyclerViewTasks.setAdapter(adapterTask);
        adapterTask.notifyDataSetChanged();
        holder.edt_newTask.setText("");
    }

    @SuppressLint({"ResourceType","MissingInflatedId"})
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

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setTitle("Edit stage");

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.row_spinner_stage,null);

                    String[] stageSequence = new String[itemStage.size()];

                    for (int i =0; i< stageSequence.length; i++){
                        stageSequence[i] = itemStage.get(i).getSequence().toString();
                    }


                    TextView title = (TextView) layout.findViewById(R.id.textTitleTxt);
                    TextView spinnerTitle = (TextView) layout.findViewById(R.id.spinnerTitleTxt);
                    EditText newStageName = (EditText) layout.findViewById(R.id.editText1);
                    CheckBox isDoneStage = (CheckBox) layout.findViewById(R.id.isDoneStage);
                    CheckBox isCancelStage = (CheckBox) layout.findViewById(R.id.isCancelStage);


                    Spinner s = (Spinner) layout.findViewById(R.id.spinner1);
                    ArrayAdapter adapter = new ArrayAdapter(context, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, stageSequence);

                    title.setText("Stage name");

                    spinnerTitle.setText("Sequence");
                    newStageName.setText(itemStage.get(currentPos).getName());
                    isDoneStage.setChecked(itemStage.get(currentPos).getIsDone());
                    isCancelStage.setChecked(itemStage.get(currentPos).getIsCancel());
                    s.setAdapter(adapter);
                    s.setSelection(itemStage.get(currentPos).getSequence());

                    builder.setView(layout);

                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Update stage sequence + stage name here
                            String newStageNameStr = String.valueOf(newStageName.getText());
                            changeStageSequence(Integer.parseInt(stageSequence[s.getSelectedItemPosition()]), newStageNameStr, isDoneStage.isChecked(),isCancelStage.isChecked());
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void changeStageSequence(int newSequence, String newStageNameStr, boolean isDone, boolean isCancel){
        Stage oldStage = itemStage.get(currentPos);
        if (!TextUtils.isEmpty(newStageNameStr))
            itemStage.get(currentPos).setName(newStageNameStr);


        itemStage.get(currentPos).setIsDone(isDone);

        Log.d("getNewSequence", String.valueOf(newSequence));
        int oldSequence = oldStage.getSequence();

        if (oldSequence < newSequence) {
            for (int i = oldSequence; i < newSequence; i++) {
                Collections.swap(itemStage, i, i + 1);
            }
        } else {
            for (int i = oldSequence; i > newSequence; i--) {
                Collections.swap(itemStage, i, i - 1);
            }
        }

        for (int i =0; i< itemStage.size(); i++){
            itemStage.get(i).setSequence(i);
            if (isDone){
                if (i != newSequence) itemStage.get(i).setIsDone(false);
                else itemStage.get(i).setIsDone(true);
            }
            if (isCancel){
                if (i != newSequence) itemStage.get(i).setIsCancel(false);
                else itemStage.get(i).setIsCancel(true);
            }
        }
        for (Stage s: itemStage) {
            Log.d("Sequence aki", String.valueOf(s.getName() + " - "+ s.getSequence()));

            JsonObject updateStage = new JsonObject();
            updateStage.addProperty("stage", s.getId());
            updateStage.addProperty("sequence", s.getSequence());
            updateStage.addProperty("name", s.getName());
            updateStage.addProperty("isDone", s.getIsDone());
            updateStage.addProperty("isCancel",s.getIsCancel());

            apiServices.updateProjectStage(APIClient.getToken(context),s.getProject(), updateStage).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()){
                        adapterStage.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }


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
